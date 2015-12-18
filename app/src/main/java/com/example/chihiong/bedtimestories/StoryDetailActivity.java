package com.example.chihiong.bedtimestories;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static android.widget.Toast.makeText;
import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

public class StoryDetailActivity extends AppCompatActivity implements View.OnClickListener, RecognitionListener {

    public static final String EXTRA_PARAM_ID = "story_id";
//    private RecyclerView mList;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView mImageView;
    private LinearLayout mTitleHolder;
    private TextView mTitle;
    private TextView mStoryParagraph;
    private FloatingActionButton mAddButton;
    private LinearLayout mContentHolder;
    private LinearLayout mRevealView;
    private EditText mEditTextTodo;
    private boolean isEditTextVisible;
    private InputMethodManager mInputManager;
    private Story mStory;
    private ArrayList<String> mTodoList;
    private ArrayAdapter mToDoAdapter;
    int defaultColor;

    private static final String LOG_TAG = StoryDetailActivity.class.getSimpleName();
    private static final String FILENAME = "keywords.txt";
    private static final int ADD_REQUEST_CODE = 0x200;
    private static final String KEYWORD_SEARCH = "keyword_search";
    private static final String DEFAULT_KEYWORD = "thunder";


    private SpeechRecognizer recognizer;
    private HashMap<String, String> mKeySounds = new HashMap<String, String>();
    private KeySoundAdapter mAdapter = new KeySoundAdapter(mKeySounds);
    private String mKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStory = StoryData.storyList().get(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));

//        mList = (RecyclerView) findViewById(R.id.list);
        mImageView = (ImageView) findViewById(R.id.story_image);
        mTitleHolder = (LinearLayout) findViewById(R.id.placeNameHolder);
        mTitle = (TextView) findViewById(R.id.textView);
        mStoryParagraph = (TextView) findViewById(R.id.story_paragraph);
        mAddButton = (FloatingActionButton) findViewById(R.id.btn_add);
        mRevealView = (LinearLayout) findViewById(R.id.llEditTextHolder);
        mEditTextTodo = (EditText) findViewById(R.id.etTodo);
        mContentHolder = (LinearLayout) findViewById(R.id.sounds_content_holder);

        mAddButton.setOnClickListener(this);
        defaultColor = getResources().getColor(R.color.colorPrimaryDark);

        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mRevealView.setVisibility(View.INVISIBLE);
        isEditTextVisible = false;

//        setUpAdapter();
        loadStory();
        windowTransition();
        getPhoto();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addDefaultKeySound("storm is coming", "thunder");
        addDefaultKeySound("everyone applauded", "applause");
        addDefaultKeySound("crickets", "crickets");
        addDefaultKeySound("crowd laughing", "crowd_laughing");
        addDefaultKeySound("ringed the door bell", "door_bell");
        addDefaultKeySound("building a table", "hammering");
        addDefaultKeySound("received a call", "phone_ringing");
        addDefaultKeySound("birds and monkeys", "birds_and_monkeys");

        mStoryParagraph.setText(Html.fromHtml(
                "<p><strong>Once upload a time, </strong>" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed dapibus nunc ut ultrices finibus. Nullam a est dignissim, eleifend ligula a, euismod nunc. Fusce aliquam nulla vel tempus pellentesque. Aliquam elementum, velit ac pharetra viverra, diam mauris lacinia libero, ut placerat nisi ipsum ac metus. Nam lobortis enim ac interdum scelerisque. Morbi sed semper lacus. Praesent id tincidunt odio. Etiam non congue tellus. Maecenas id magna consequat, commodo ligula eget, tincidunt odio. Nullam urna ipsum, euismod at erat eget, viverra egestas mauris. Vivamus feugiat odio et cursus luctus. Proin nisi odio, lobortis vel dui in, vulputate imperdiet urna.</p>"+
                "<p>Aenean eget pretium nisi. Donec efficitur lorem eros, non egestas felis volutpat sit amet. Vivamus non tempus lacus. Duis vulputate ultricies nisi, id elementum tortor elementum eu. Phasellus et efficitur felis. Morbi ultricies pulvinar arcu, eget facilisis odio maximus vitae. Quisque ultrices vehicula nulla nec hendrerit. Etiam aliquam mattis sapien vel euismod. In non rhoncus est, nec tempus massa. Suspendisse potenti. Nunc ac velit sodales, gravida justo sed, tempor dolor. Nam a leo suscipit, porttitor velit eu, efficitur ipsum. Proin eu purus erat. Aenean eget gravida massa, at feugiat tortor. Sed venenatis malesuada libero, vel sollicitudin massa.</p>" +
                "<p>Aenean imperdiet et leo vitae ultrices. Quisque molestie est augue, non consequat ligula pretium eu. Aenean et ligula tellus. Cras tincidunt sapien at sapien mollis, ac condimentum magna vestibulum. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Cras facilisis ante sem, quis fermentum sapien faucibus suscipit. Ut in nunc nisi. Curabitur semper vestibulum sapien, elementum consectetur nisl. Quisque sed tellus vitae massa pellentesque dapibus a in tortor. Fusce at lectus est. Sed vitae neque orci. Phasellus molestie lacus a risus tempor, et ullamcorper dui sodales. Integer eget tincidunt lectus. Integer a ipsum lectus.</p>"
        ));

//        mLayoutManager = new LinearLayoutManager(this);
//        mList.setNestedScrollingEnabled(false);
//        mList.setHasFixedSize(false);

//        mList.setLayoutManager(mLayoutManager);
//        mList.setLayoutManager(new WrappingLinearLayoutManager(this));
//        mList.setAdapter(mAdapter);
//        mAdapter.setListViewHeightBasedOnChildren(mList);

    }

    public class InitRecognizer extends AsyncTask<Void, Void, Exception> {

        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(StoryDetailActivity.this);
                File assetDir = assets.syncAssets();
                setupRecognizer(assetDir);
            } catch (IOException e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
                Toast.makeText(getApplicationContext(), "Failed to init recognizer " + result, Toast.LENGTH_SHORT).show();
            } else {
                mTitle.setText("Listening...");
                Toast.makeText(getApplicationContext(), "Ready!", Toast.LENGTH_SHORT).show();
                listen();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recognizer.cancel();
        recognizer.shutdown();
    }



    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        Iterator it = mAdapter.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
            String keyword = pair.getKey();
            if (text.equals(keyword)) {
                listen();
            }
        }

        Log.d(LOG_TAG, text);
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            Iterator it = mAdapter.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
                String keyword = pair.getKey();
                String key = keyword;
                int last = key.indexOf("/1e-");
                if (last >= 0) {
                    key = key.substring(0, last);
                }
                if (text.indexOf(key) >= 0) {
                    Log.i(LOG_TAG, "Playing sound");
                    playSound(keyword);
                    listen();
                    break;
                }
                Log.i(LOG_TAG, "Testing text '" + text + "' with key '" + key + "'");
            }
            makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "Speech starting");
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "Reached end of speech");
        listen();
    }

    private void listen() {
        if (recognizer != null) {
            recognizer.stop();
            if (mAdapter.size() > 0) {
                recognizer.startListening(KEYWORD_SEARCH, 5000);
            }
        }
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .setKeywordThreshold(1e-45f) // Threshold to tune for keyphrase to balance between false alarms and misses
                .setBoolean("-allphone_ci", true)// Use context-independent phonetic search, context-dependent is too slow for mobile
                .getRecognizer();
        recognizer.addListener(StoryDetailActivity.this);

        loadRecognizerKeywordsFile();
    }

    @Override
    public void onError(Exception error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeout() {
        listen();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                String uriStr = uri.toString();
                Log.i(LOG_TAG, "Uri: " + uriStr);
                saveKeySound(mKeyword, uriStr);
            }
        }
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add, null);

        mKeyword = null;

        builder.setMessage(R.string.add_dialog_message);
        builder.setTitle(R.string.add_dialog_title);
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText edit = (EditText) view.findViewById(R.id.keyword);
                String keyword = edit.getText().toString();
                if (keyword.length() < 3) {
                    Toast.makeText(getApplicationContext(), "Keyword length is too short!", Toast.LENGTH_SHORT).show();
                }

                String[] words = keyword.split(" ");
                for (String word : words) {
                    if (recognizer.getDecoder().lookupWord(word) == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't find the word '" + word + "' in dictionary", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                chooseKeySound(keyword);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void addDefaultKeySound(String s, String filename) {
        String keyword = s + calculateThreshold(s) + "\n";
        saveKeySound(keyword, filename);
    }

    private void chooseKeySound(String keyword) {
        mKeyword = keyword + calculateThreshold(keyword) + "\n";

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");

        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    private void saveKeySound(String keyword, String uri) {
        mAdapter.put(keyword, uri);
        mAdapter.notifyDataSetChanged();

        saveRecognizerKeywordsFile();
        loadRecognizerKeywordsFile();
    }

    private void deleteAllKeySounds() {
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();

        saveRecognizerKeywordsFile();
        loadRecognizerKeywordsFile();
    }

    private void playSound(String keyword) {
        String uriStr = mAdapter.get(keyword);
        if (!uriStr.contains("//")) {
            int resID = getResources().getIdentifier(uriStr, "raw", getPackageName());
            MediaPlayer mediaPlayer = MediaPlayer.create(this, resID);
            mediaPlayer.start();
        } else {
            Uri uri = Uri.parse(uriStr);
            Log.i(LOG_TAG, "Playing sound: " + uriStr + " triggered by '" + keyword + "'");
            Ringtone ring = RingtoneManager.getRingtone(this, uri);
            if (ring != null) {
                ring.play();
            }
        }
    }

    private void saveRecognizerKeywordsFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            Iterator it = mAdapter.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
                String key = pair.getKey();
                fos.write(key.getBytes());
                Log.i(LOG_TAG, "Saved keyword: " + key);
            }
            fos.close();
            Log.i(LOG_TAG, "Saved " + mAdapter.size() + " keywords!");
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File not found: " + FILENAME, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRecognizerKeywordsFile() {
        if (recognizer != null && mAdapter.size() > 0) {
            recognizer.stop();

            File file = new File(getFilesDir(), FILENAME);
            if (file.exists()) {
                Log.i(LOG_TAG, "Setting keyword list");
                recognizer.addKeywordSearch(KEYWORD_SEARCH, file);
            }

            listen();
        }
    }

    private String calculateThreshold(String key) {
        final int max = 5;
        final int mult = 5;
        int n = 1;
        int len = key.length();

        for (int i = 1; i < max; i++) {
            if (len >= i * mult && len < (i + 1) * mult) {
                n = i * 10;
            } else if (len >= max * mult) {
                n = max * mult;
            }
        }

        n += (key.split(" ").length - 1) * 10;

        return "/1e-" + n + "/";
    }

    private void getPhoto() {
        Bitmap photo = BitmapFactory.decodeResource(getResources(), mStory.getImageResourceId(this));
        colorize(photo);
    }

    private void colorize(Bitmap photo) {
        Palette mPalette = Palette.generate(photo);
        applyPalette(mPalette);
    }

    private void applyPalette(Palette mPalette) {
        getWindow().setBackgroundDrawable(new ColorDrawable(mPalette.getDarkMutedColor(defaultColor)));
        mTitleHolder.setBackgroundColor(mPalette.getMutedColor(defaultColor));
        mRevealView.setBackgroundColor(mPalette.getLightVibrantColor(defaultColor));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void windowTransition() {
        getWindow().getEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                mAddButton.animate().alpha(1.0f);
                getWindow().getEnterTransition().removeListener(this);
            }
        });
    }

    private void loadStory() {

        Bitmap photo = BitmapFactory.decodeResource(this.getResources(), mStory.getImageResourceId(this));

        Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
//                int bgColor = palette.getVibrantColor(getResources().getColor(android.R.color.black));
                int holderColor = palette.getLightMutedColor(getResources().getColor(android.R.color.black));
//                mTitleHolder.setBackgroundColor(bgColor);
                mContentHolder.setBackgroundColor(holderColor);
            }
        });



        getSupportActionBar().setTitle(mStory.title);

        mImageView.setImageResource(mStory.getImageResourceId(this));
    }

    private void setUpAdapter() {
        mTodoList = new ArrayList<>();
        mToDoAdapter = new ArrayAdapter(this, R.layout.row_todo, mTodoList);
//        mList.setAdapter(mToDoAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                Animatable mAnimatable;

                InitRecognizer initRecognizer = new InitRecognizer();
                initRecognizer.execute();
                mAddButton.setImageResource(R.drawable.icn_morph);
                mAnimatable = (Animatable) (mAddButton).getDrawable();
                mAnimatable.start();

//                if (!isEditTextVisible) {
//                    revealEditText(mRevealView);
//                    mEditTextTodo.requestFocus();
//                    mInputManager.showSoftInput(mEditTextTodo, InputMethodManager.SHOW_IMPLICIT);
//
//                    mAddButton.setImageResource(R.drawable.icn_morph);
//                    mAnimatable = (Animatable) (mAddButton).getDrawable();
//                    mAnimatable.start();
//                } else {
//                    addToDo(mEditTextTodo.getText().toString());
//                    mToDoAdapter.notifyDataSetChanged();
//                    mInputManager.hideSoftInputFromWindow(mEditTextTodo.getWindowToken(), 0);
//                    hideEditText(mRevealView);
//
//                    mAddButton.setImageResource(R.drawable.icn_morph_reverse);
//                    mAnimatable = (Animatable) (mAddButton).getDrawable();
//                    mAnimatable.start();
//                }
        }
    }

    private void addToDo(String todo) {
        mTodoList.add(todo);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void hideEditText(final LinearLayout mRevealView) {
        int cx = mRevealView.getRight() - 30;
        int cy = mRevealView.getBottom() - 60;
        int initialRadius = mRevealView.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mRevealView.setVisibility(View.INVISIBLE);
            }
        });
        isEditTextVisible = false;
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealEditText(LinearLayout mRevealView) {
        int cx = mRevealView.getRight() - 30;
        int cy = mRevealView.getBottom() - 60;
        int finalRadius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, finalRadius);
        mRevealView.setVisibility(View.VISIBLE);
        isEditTextVisible = true;
        anim.start();
    }

    @Override
    public void onBackPressed() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(100);
        mAddButton.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd(Animation animation) {
                mAddButton.setVisibility(View.GONE);
                finishAfterTransition();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
