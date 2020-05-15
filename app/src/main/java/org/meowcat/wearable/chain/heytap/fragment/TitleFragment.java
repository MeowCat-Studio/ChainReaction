package org.meowcat.wearable.chain.heytap.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.meowcat.wearable.chain.R;
import org.meowcat.wearable.chain.heytap.activity.NewGameActivity;
import org.meowcat.wearable.chain.heytap.adapter.ChessAdapter;
import org.meowcat.wearable.chain.heytap.game.Control;
import org.meowcat.wearable.chain.heytap.model.ChessModel;
import org.meowcat.wearable.chain.heytap.util.MyApplication;
import org.meowcat.wearable.chain.heytap.util.SharedPreferencesUtil;

import java.util.Random;


public class TitleFragment extends Fragment implements View.OnClickListener
{
    private Context ctx;
    private View rootLayout;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private TitleFragmentListener titleFragmentListener;
    private ChessModel chessModel;
    private Control control;
    private Random random;

    private RecyclerView recyclerView;
    private ChessAdapter chessAdapter;

    private Handler handler = new Handler();
    private Runnable runnableRotation;

    private int animSpeed;

    public TitleFragment() {}

    public static TitleFragment newInstance()
    {
        return new TitleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ctx = MyApplication.getContext();
        rootLayout = inflater.inflate(R.layout.fragment_title, container, false);
        sharedPreferencesUtil = new SharedPreferencesUtil();

        chessModel = ChessModel.newInstanceFormNewGame(3, true, 3, -1);
        control = new Control(chessModel, false);
        random = new Random();

        recyclerView = rootLayout.findViewById(R.id.title_chess);
        chessAdapter = new ChessAdapter(chessModel, control, animSpeed);
        GridLayoutManager layoutManager = new GridLayoutManager(ctx, chessModel.getChessSize());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chessAdapter);

        runnableRotation = new Runnable()
        {
            @Override
            public void run()
            {
                if(control.getNext()[0] >= 0)
                {
                    control.moveChess();
                    control.apply();
                    chessAdapter.notifyItemRangeChanged(0, chessModel.getChessSize() * chessModel.getChessSize(), false);
                    handler.postDelayed(this, animSpeed * 200 + 300);
                }
                else
                    move();
            }
        };
        move();

        if(!sharedPreferencesUtil.contains(SharedPreferencesUtil.game))
            rootLayout.findViewById(R.id.title_continue).setEnabled(false);

        rootLayout.findViewById(R.id.title_new).setOnClickListener(this);
        rootLayout.findViewById(R.id.title_continue).setOnClickListener(this);
        rootLayout.findViewById(R.id.title_tutorial).setOnClickListener(this);
        rootLayout.findViewById(R.id.title_menu).setOnClickListener(this);

        return rootLayout;
    }

    private void move()
    {
        control.setNext(new int[]{random.nextInt(2) == 0 ? 0 : chessModel.getChessSize() - 1, chessModel.getChessSize() / 2});
        control.setAngle(random.nextInt(2) == 0 ? -1 : 1);
        handler.post(runnableRotation);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.title_new)
        {
            Intent intent = new Intent(ctx, NewGameActivity.class);
            startActivityForResult(intent, 0);
        }
        else if(view.getId() == R.id.title_continue)
            titleFragmentListener.onTitleFragmentStart(ChessModel.newInstanceFormJSON(sharedPreferencesUtil.getString(SharedPreferencesUtil.game, "{}")));
        else if(view.getId() == R.id.title_tutorial)
            titleFragmentListener.onTitleFragmentTutorial();
        else if(view.getId() == R.id.title_menu)
            titleFragmentListener.onTitleFragmentMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            sharedPreferencesUtil.removeValue(SharedPreferencesUtil.game);
            titleFragmentListener.onTitleFragmentStart((ChessModel) data.getSerializableExtra("chessModel"));
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof TitleFragmentListener)
        {
            titleFragmentListener = (TitleFragmentListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement TitleFragmentListener");
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        animSpeed = sharedPreferencesUtil.getInt(SharedPreferencesUtil.animSpeed, 1);
        if(chessAdapter != null)
            chessAdapter.setAnimSpeed(animSpeed);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        titleFragmentListener = null;
    }

    public interface TitleFragmentListener
    {
        void onTitleFragmentStart(ChessModel chessModel);
        void onTitleFragmentTutorial();
        void onTitleFragmentMenu();
    }
}
