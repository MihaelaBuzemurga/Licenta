package com.example.gabriel.readerlish;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.artifex.mupdfdemo.Annotation;
import com.artifex.mupdfdemo.FilePicker;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.MuPDFView;
import com.artifex.mupdfdemo.PageView;
import com.artifex.mupdfdemo.SearchTask;
import com.artifex.mupdfdemo.SearchTaskResult;
import com.example.gabriel.readerlish.Carte.Carte;
import com.example.gabriel.readerlish.ConnectionManager.ConnectionManager;
import com.example.gabriel.readerlish.Mesaj.Mesaj;
import com.example.gabriel.readerlish.Mesaj.RequestEnum;
import com.example.gabriel.readerlish.User.User;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Map;


public class ReadActivity extends AppCompatActivity {
    enum AcceptMode {Highlight, Underline, StrikeOut, Ink, CopyText};
    enum Type_me {
        TEXT, LINK, FREETEXT, LINE, SQUARE, CIRCLE, POLYGON, POLYLINE, HIGHLIGHT,
        UNDERLINE, SQUIGGLY, STRIKEOUT, STAMP, CARET, INK, POPUP, FILEATTACHMENT,
        SOUND, MOVIE, WIDGET, SCREEN, PRINTERMARK, TRAPNET, WATERMARK, A3D, UNKNOWN
    }
    MuPDFCore core;
    private TextView     mPageNumberView;
    private SeekBar      mPageSlider;
    private int          mPageSliderRes;
    MuPDFReaderView      reader;
    private SearchTask   mSearchTask;
    private int          m_IdCarte;
    ProgressDialog       m_pd;
    GetBookTask          m_getBookTask;
    Carte                m_carte;
    private ImageButton  mSearchBack;
    private ImageButton  mSearchFwd;
    private ImageButton  m_EditMode;

    private int nrGraphPage;
    private int nrAllGraphPage;
    ViewAnimator viewAnimator;
    EditText m_editSearch;
    ImageButton m_buttonSearch;
    ImageButton m_buttonSearch_fromView;
    ImageButton m_buttonSearch_close;
    ImageButton m_copyButton;
    FloatingActionButton m_sentiButton;
    private AcceptMode   mAcceptMode;
    Button m_buttonPrev;
    Button m_buttonNext;
    RelativeLayout m_LayoutBook;
    GetSenti m_getSentiTask;

    ViewAnimator m_viewAnimator;

    Map<String,Double> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Intent myIntent = getIntent();
        m_IdCarte=myIntent.getIntExtra("id_carte",0);

        getUi();
        setProgressDialog();
        startGetBook();
        setListener();
        m_sentiButton.setVisibility(View.INVISIBLE);
    }
    void setBookView()
    {
        core= null;
        try {
            core = new MuPDFCore(this, m_carte.getContinut(),m_carte.getNume());
        } catch (Exception e) {
            e.printStackTrace();
        }
        reader = new MuPDFReaderView(this)
        {
            @Override
            protected void onMoveToChild(int i) {
                if (core == null)
                    return;

                mPageNumberView.setText(String.format("%d / %d", i + 1,
                        core.countPages()));
                mPageSlider.setMax((core.countPages() - 1) * mPageSliderRes);
                mPageSlider.setProgress(i * mPageSliderRes);
                super.onMoveToChild(i);
            }


        };
       // reader.setMode(MuPDFReaderView.Mode.Selecting);


        reader.setAdapter(new MuPDFPageAdapter(this, new FilePicker.FilePickerSupport() {
            @Override
            public void performPickFor(FilePicker filePicker) {
                System.out.println("qwewq");
            }
        }, core));

        int smax = Math.max(core.countPages()-1,1);
        mPageSliderRes = ((10 + smax - 1)/smax) * 2;
        mPageSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                reader.setDisplayedViewIndex((seekBar.getProgress()+mPageSliderRes/2)/mPageSliderRes);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                updatePageNumView((progress+mPageSliderRes/2)/mPageSliderRes);
            }
        });
        reader.setScrollBarSize(2);
        m_LayoutBook.addView(reader);
        if (isProofing()) {
            //  go to the current page
            int currentPage = getIntent().getIntExtra("startingPage", 0);
            reader.setDisplayedViewIndex(currentPage);
        }
        mSearchTask = new SearchTask(this, core) {
            @Override
            protected void onTextFound(SearchTaskResult result) {
                SearchTaskResult.set(result);
                // Ask the ReaderView to move to the resulting page
                reader.setDisplayedViewIndex(result.pageNumber);
                // Make the ReaderView act on the change to SearchTaskResult
                // via overridden onChildSetup method.
                reader.resetupChildren();
            }
        };
    }
    public boolean isProofing()
    {
        String format = core.fileFormat();
        return (format.equals("GPROOF"));
    }
    private void updatePageNumView(int index) {
        if (core == null)
            return;
        mPageNumberView.setText(String.format("%d / %d", index + 1, core.countPages()));
    }
    void getUi()
    {
        mPageNumberView=(TextView) findViewById(R.id.textView_pageNumber);
        mPageSlider=(SeekBar)findViewById(R.id.seekBar);
        m_LayoutBook = (RelativeLayout) findViewById(R.id.layout_ViewBook);
        m_buttonSearch=(ImageButton)findViewById(R.id.button_searchWord);

        mSearchBack = (ImageButton)findViewById(R.id.searchBack);
        mSearchFwd = (ImageButton)findViewById(com.artifex.mupdfdemo.R.id.searchForward);
        viewAnimator = (ViewAnimator)findViewById(R.id.viewAnimator);


        m_editSearch= (EditText) findViewById(R.id.m_searchText);

       m_buttonSearch_fromView=(ImageButton)findViewById(R.id.imageButton_search);
        m_buttonSearch_close=(ImageButton)findViewById(R.id.imageButton_CloseSearch);
       m_buttonPrev=(Button)findViewById(R.id.imageButton_Prev);
        m_buttonNext=(Button)findViewById(R.id.imageButton_Next);
        m_EditMode=(ImageButton)findViewById(R.id.imageButton_Edit);
        m_copyButton=(ImageButton)findViewById(R.id.imageButton_copy);

        m_sentiButton=(FloatingActionButton)findViewById(R.id.floatingActionButton2);



    }
    private void setListener()
    {

        m_buttonSearch_fromView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                if(TextUtils.isEmpty(m_editSearch.getText())) {
                    Toast.makeText(getApplicationContext(), "Introdu textul pentru a cauta", Toast.LENGTH_LONG).show();
                }
                else
                {
                    int displayPage = reader.getDisplayedViewIndex();
                    SearchTaskResult r = SearchTaskResult.get();
                    int searchPage = r != null ? r.pageNumber : -1;
                    mSearchTask.go(m_editSearch.getText().toString(), 0, displayPage, searchPage);
                    viewAnimator.setDisplayedChild(1);
                }
            }
        });
        m_EditMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAnimator.setDisplayedChild(2);
                Toast.makeText(getApplicationContext(), "Edit mode", Toast.LENGTH_LONG).show();


            }
        });
        m_buttonSearch_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAnimator.setDisplayedChild(0);

            }
        });
        m_buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(-1);
            }
        });
        m_buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(1);
            }
        });
        m_buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAnimator.setDisplayedChild(1);
                Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_LONG).show();
            }
        });

        m_copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MuPDFView pageView = (MuPDFView) reader.getDisplayedView();
                boolean success = false;
                if (pageView != null) {
                    success = pageView.copySelection();

                }
                if(success)
                {
                    m_sentiButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Copy", Toast.LENGTH_LONG).show();
                }
                else
                {
                    m_sentiButton.setVisibility(View.INVISIBLE);
                }
            }
        });


        m_sentiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if(clipboard.hasText())
                {
                    m_pd.show();
                    m_getSentiTask =new GetSenti(clipboard.getText().toString());
                    clipboard.setText("");
                    m_getSentiTask.execute((Void) null);
                }


            }
        });

    }
    private void search(int direction) {
        int displayPage = reader.getDisplayedViewIndex();
        SearchTaskResult r = SearchTaskResult.get();
        int searchPage = r != null ? r.pageNumber : -1;
        mSearchTask.go(m_editSearch.getText().toString(), direction, displayPage, searchPage);
    }
    private void startGetBook()
    {
        m_pd.show();
        m_getBookTask =new GetBookTask(m_IdCarte);
        m_getBookTask.execute((Void) null);
    }
    private void setProgressDialog()
    {
        m_pd= new ProgressDialog(this);
        m_pd.setTitle("Incarcare!");
        m_pd.setMessage("Asteapta");
    }

    public void closeUtils(View view) {
        viewAnimator.setDisplayedChild(0);
        m_sentiButton.setVisibility(View.INVISIBLE);
    }

    public void OnInkButtonClick(View view) {
        mAcceptMode =AcceptMode.Ink;
        reader.setMode(MuPDFReaderView.Mode.Selecting);
        Toast.makeText(getApplicationContext(), "Selecting mode", Toast.LENGTH_LONG).show();
    }

    public void OnUnderlineButtonClick(View view) {
        mAcceptMode =AcceptMode.Underline;
        reader.setMode(MuPDFReaderView.Mode.Selecting);
        Toast.makeText(getApplicationContext(), "Underline mode", Toast.LENGTH_LONG).show();
    }

    public void OnHighlightButtonClick(View view) {
        mAcceptMode =AcceptMode.Highlight;
        reader.setMode(MuPDFReaderView.Mode.Selecting);
        Toast.makeText(getApplicationContext(), "Highlight mode", Toast.LENGTH_LONG).show();
    }
    private void showGraph()
    {
        Dialog m_dialogGraph=new Dialog(this);
        m_dialogGraph.setTitle("Carte");
        m_dialogGraph.setContentView(R.layout.layout_senti);
        ImageButton m_prevGraph=(ImageButton)m_dialogGraph.findViewById(R.id.m_imageButton_senti_prev);
        ImageButton m_nextGraph=(ImageButton)m_dialogGraph.findViewById(R.id.m_imageButton_senti_next);
        m_viewAnimator=(ViewAnimator) m_dialogGraph.findViewById(R.id.m_viewAnimator);
        nrGraphPage=0;
        nrAllGraphPage=map.size();
        m_prevGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nrGraphPage>0)
                {
                    nrGraphPage--;
                    m_viewAnimator.setDisplayedChild(nrGraphPage);
                }
            }
        });


        m_nextGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nrGraphPage<nrAllGraphPage)
                {
                    nrGraphPage++;
                    m_viewAnimator.setDisplayedChild(nrGraphPage);

                }

            }
        });


        for(Map.Entry m:map.entrySet()){
            GraphView graph = new GraphView(m_dialogGraph.getContext());
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                    new DataPoint(0, (double)m.getValue()),
            });
            graph.addSeries(series);
            // styling
            series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                @Override
                public int get(DataPoint data) {
                    return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
                }
            });

            series.setSpacing(50);
            series.setTitle((String)m.getKey());
            // draw values on top
            series.setDrawValuesOnTop(true);
            series.setValuesOnTopColor(Color.RED);

            //series.setValuesOnTopSize(50);
            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

            m_viewAnimator.addView(graph);
        }
        m_dialogGraph.show();
        m_sentiButton.setVisibility(View.INVISIBLE);





    }
    public void OnAcceptButtonClick(View view) {
        MuPDFView pageView = (MuPDFView) reader.getDisplayedView();
        boolean success = false;
        switch (mAcceptMode) {
            case Highlight:
                if (pageView != null)
                    success = pageView.markupSelection(Annotation.Type.HIGHLIGHT);
                break;

            case Underline:
                if (pageView != null)
                    success = pageView.markupSelection(Annotation.Type.UNDERLINE);
                break;

            case StrikeOut:
                if (pageView != null)
                    success = pageView.markupSelection(Annotation.Type.STRIKEOUT);

                break;

            case Ink:
                if (pageView != null)
                    success = pageView.saveDraw();
                break;
        }
        reader.setMode(MuPDFReaderView.Mode.Viewing);
    }

    public class GetBookTask extends AsyncTask<Void, Void, Boolean> {
        private Carte carte;
        private Mesaj m_mesaj;
        GetBookTask(int nr) {
            carte=new Carte();
            carte.setID(nr);
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            Mesaj mesaj=new Mesaj();
            mesaj.setObiect(carte);
            mesaj.setCmd(RequestEnum.REQUEST_FILE_CONTENT);
            m_mesaj= (Mesaj) ConnectionManager.getInstance().sendMessage(mesaj);
            if(m_mesaj!=null) {

                return true;
            }
            else
            {
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            m_pd.cancel();
            m_getBookTask = null;
            if(success)
            {
                m_carte=(Carte)m_mesaj.getObiect();
                setBookView();
            }
        }
        @Override
        protected void onCancelled() {
            m_getBookTask = null;
            m_pd.cancel();
        }
    }



    public class GetSenti extends AsyncTask<Void, Void, Boolean> {
        private String m_words;
        private Mesaj m_mesaj;
        GetSenti(String value) {
            m_words=value;

        }
        @Override
        protected Boolean doInBackground(Void... params) {
            Mesaj mesaj=new Mesaj();
            mesaj.setObiect(m_words);
            mesaj.setCmd(RequestEnum.REQUEST_SENTI);
            m_mesaj= (Mesaj) ConnectionManager.getInstance().sendMessage(mesaj);
            if(m_mesaj!=null) {

                return true;
            }
            else
            {
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            m_pd.cancel();
            m_getBookTask = null;
            if(success)
            {
                map=(Map<String,Double>)m_mesaj.getObiect();
                showGraph();

            }
        }
        @Override
        protected void onCancelled() {
            m_getBookTask = null;
            m_pd.cancel();
        }
    }

}
