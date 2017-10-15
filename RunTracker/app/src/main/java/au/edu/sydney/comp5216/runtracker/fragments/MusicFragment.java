package au.edu.sydney.comp5216.runtracker.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import au.edu.sydney.comp5216.runtracker.R;
import au.edu.sydney.comp5216.runtracker.adapters.MediaCursorAdapter;

/**
 * Created by pranav on 29/09/2017.
 * Reference Reference http://indragni.com
 * Reference https://youtu.be/q1sCQMCyZtU
 */

public class MusicFragment extends Fragment {

    View view;
    public static final int MY_PERMISSIONS_REQUEST_MUSIC = 100;

    private static final int UPDATE_FREQUENCY = 500;
    private static final int STEP_VALUE = 4000;

    private MediaCursorAdapter mediaAdapter = null;
    private ListView songList;
    private TextView currentSong = null;
    private SeekBar seekbar = null;
    private MediaPlayer player = null;
    private ImageButton playButton = null;
    private ImageButton prevButton = null;
    private ImageButton nextButton = null;

    private boolean isStarted = true;
    private String currentFile = "";
    private boolean isMoveingSeekBar = false;

    private final Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_music, container, false);

        songList = view.findViewById(R.id.songList);
        currentSong = view.findViewById(R.id.currentSong);
        seekbar = view.findViewById(R.id.seekBar);
        playButton = view.findViewById(R.id.playPause);
        prevButton = view.findViewById(R.id.previous);
        nextButton = view.findViewById(R.id.next);

        player = new MediaPlayer();

        player.setOnCompletionListener(onCompletion);
        player.setOnErrorListener(onError);
        seekbar.setOnSeekBarChangeListener(seekBarChanged);

        songList.setOnItemClickListener(onListItemClick);
        playButton.setOnClickListener(onButtonClick);
        nextButton.setOnClickListener(onButtonClick);
        prevButton.setOnClickListener(onButtonClick);

        fetchSongs();
        return view;
    }

    private void checkExternalPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_MUSIC);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                fetchSongs();
            }
        }
    }

    private final Runnable updatePositionRunnable = new Runnable() {
        public void run() {
            updatePosition();
        }
    };

    private void fetchSongs() {
        if (hasPermissionStorage()) {

            Cursor cursor = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

            if (null != cursor) {
                cursor.moveToFirst();

                mediaAdapter = new MediaCursorAdapter(getContext(), R.layout.layout_song_item, cursor);

                songList.setAdapter(mediaAdapter);
            }
        } else {
            checkExternalPermission();
        }
    }

    private boolean hasPermissionStorage() {
        return (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(updatePositionRunnable);
        player.stop();
        player.reset();
        player.release();

        player = null;
    }

    private void startPlay(String file) {
        Log.i("Selected: ", file);

        currentSong.setText(file);
        seekbar.setProgress(0);

        player.stop();
        player.reset();

        try {
            player.setDataSource(file);
            player.prepare();
            player.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        seekbar.setMax(player.getDuration());
        playButton.setImageResource(android.R.drawable.ic_media_pause);

        updatePosition();

        isStarted = true;
    }

    private void stopPlay() {
        player.stop();
        player.reset();
        playButton.setImageResource(android.R.drawable.ic_media_play);
        handler.removeCallbacks(updatePositionRunnable);
        seekbar.setProgress(0);

        isStarted = false;
    }

    private void updatePosition() {
        handler.removeCallbacks(updatePositionRunnable);

        seekbar.setProgress(player.getCurrentPosition());

        handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
    }


    private AdapterView.OnItemClickListener onListItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            currentFile = (String) view.getTag();
            startPlay(currentFile);
        }
    };

    private View.OnClickListener onButtonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playPause: {
                    if (player.isPlaying()) {
                        handler.removeCallbacks(updatePositionRunnable);
                        player.pause();
                        playButton.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        if (isStarted) {
                            player.start();
                            playButton.setImageResource(android.R.drawable.ic_media_pause);

                            updatePosition();
                        } else {
                            startPlay(currentFile);
                        }
                    }

                    break;
                }
                case R.id.next: {
                    int seekto = player.getCurrentPosition() + STEP_VALUE;

                    if (seekto > player.getDuration())
                        seekto = player.getDuration();

                    player.pause();
                    player.seekTo(seekto);
                    player.start();

                    break;
                }
                case R.id.previous: {
                    int seekto = player.getCurrentPosition() - STEP_VALUE;

                    if (seekto < 0)
                        seekto = 0;

                    player.pause();
                    player.seekTo(seekto);
                    player.start();

                    break;
                }
            }
        }
    };

    private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            stopPlay();
        }
    };

    private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            return false;
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChanged = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isMoveingSeekBar = false;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isMoveingSeekBar = true;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (isMoveingSeekBar) {
                player.seekTo(progress);

                Log.i("OnSeekBarChangeListener", "onProgressChanged");
            }
        }
    };
}
