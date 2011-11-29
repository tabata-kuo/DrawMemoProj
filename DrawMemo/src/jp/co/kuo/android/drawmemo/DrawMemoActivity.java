package jp.co.kuo.android.drawmemo;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class DrawMemoActivity extends Activity {
	// �����o�ϐ�
	private List<Point>	m_drawPoint = new ArrayList<Point>();
	DrawView	m_drawView;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        m_drawView = new DrawView(getApplication());
        setContentView( m_drawView );
        
        //setContentView(R.layout.drawmemo);
    }
    
    //	--------------------------
    // 		�r���[�`�揈��
    //	--------------------------
    class DrawView extends View
    {
    	// �����o�ϐ�
    	Bitmap	m_bmp = null;
    	Point oldpos = new Point(-1,-1);
    	Canvas 		m_bmpCanvas;    	
    	
    	// �R���g���X�^
		public DrawView(Context context) 
		{
			super(context);
			m_drawPoint.clear();
			// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		}
    	
		@Override
		protected void onDraw(Canvas ccanvas) 
		{
			// TODO �����������ꂽ���\�b�h�E�X�^�u
			//super.onDraw(canvas);
/*			
			ccanvas.drawColor(Color.WHITE);

			Paint paint = new Paint();
			paint.setColor( Color.BLUE );
			paint.setStyle( Paint.Style.FILL );
			
			Point endPoint = new Point( -1, -1 );
			for ( int i = 0; i < m_drawPoint.size(); i++ ) {
				Point p = m_drawPoint.get( i );
				
				if ( p.x >= 0 ) {
					if ( endPoint.x < 0 ) {
						endPoint = p;
					}
					//ccanvas.drawCircle(p.x, p.y, 10, paint);
					ccanvas.drawLine(endPoint.x, endPoint.y, p.x, p.y, paint);
				}
				endPoint = p;
			}	
*/			
			ccanvas.drawBitmap(m_bmp, 0, 0, null);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) 
		{
/*			
			// TODO �����������ꂽ���\�b�h�E�X�^�u
			//return super.onTouchEvent(event);
			
			int x = (int)event.getX();
			int y = (int)event.getY();
			
			m_drawPoint.add( new Point( x, y ) );
			
			if ( event.getAction() == MotionEvent.ACTION_UP ) {
				m_drawPoint.add( new Point( -1, -1 ) );
			}
			
			// �ĕ`��
			invalidate();
*/
			 // �`��ʒu�̊m�F
			 Point cur = new Point((int)event.getX(), (int)event.getY());
			 if (oldpos.x < 0) { 
				 oldpos = cur; 
			}
			 // �`�摮����ݒ�
			 Paint paint = new Paint();
			 paint.setColor(Color.BLUE);
			 paint.setStyle(Paint.Style.FILL);
			 paint.setStrokeWidth(4);
			 // ����`��
			 m_bmpCanvas.drawLine(oldpos.x, oldpos.y, cur.x, cur.y, paint);
			 oldpos = cur;
			 // �w�������グ������W�����Z�b�g
			 if (event.getAction() == MotionEvent.ACTION_UP) {
				 oldpos = new Point(-1, -1);
			 }
			 invalidate();
			 
			return true;
		}
		
		// ���X�g�̑S����
		public int clearDrawList()
		{
			int err = 0;
			m_drawPoint.clear();
			
			// �ĕ`��
			invalidate();
			
			return err;
		}
		
		/** ��ʃT�C�Y���ύX���ꂽ�� */
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w,h,oldw,oldh);
			m_bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			m_bmpCanvas = new Canvas(m_bmp);
			m_bmpCanvas.drawColor(Color.WHITE);			
		}		
		
		// �摜��ۑ�
		public int saveToFile()
		{
			
			int 	err = 0;	
			File	file;
			String 	strSdStatus = Environment.getExternalStorageState();
			String 	strFilename = "";
			Date 	date = new Date();

			String	strDir = Environment.getExternalStorageDirectory().getPath() + "/DrawNoteK/";
			//String SDFILE = LOGDIR+"log.txt";			
	
			if ( strSdStatus.equals( Environment.MEDIA_MOUNTED ) ) {
				//file = new File("/sdcard/DrawNoteK");
				file = new File( strDir );
				file.mkdir();				
			}
			else {
				file = Environment.getDataDirectory();
			}
			
			strFilename = file.getAbsolutePath() + "/";
			strFilename += String.format("%4d%02d%02d-%02d%02d%02d.png",
					(1900+date.getYear()), date.getMonth(), date.getDate(),
					date.getHours(), date.getMinutes(), date.getSeconds());
			
			try{
				FileOutputStream  fOut = new FileOutputStream( strFilename );	
				m_bmp.compress(CompressFormat.PNG, 100, fOut);
				fOut.flush();
				fOut.close();
				
			}catch( Exception e ) {
				assert(false);
			
					
			}
		
			
			return err;
		}		
    }
    
    // �I�v�V�������j���[�\��
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreateOptionsMenu(menu);
		
		MenuItem restItem = menu.add( 0,0,0, "Reset" );
		restItem.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		
		MenuItem saveItem = menu.add( 0,1,0, "Save" );
		saveItem.setIcon(android.R.drawable.ic_menu_save);
	
		return true;
	}
	
	// �I�v�V�������j���[���s
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		switch( item.getItemId() ) {
		// Reset
		case 0:
			m_drawView.clearDrawList();
			//m_drawPoint.clear();
			//invalidate();
			break;
			
		// Save			
		case 1:
			m_drawView.saveToFile();
			break;			
		}
		return true;
	}
}