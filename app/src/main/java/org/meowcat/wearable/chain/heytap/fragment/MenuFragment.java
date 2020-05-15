package org.meowcat.wearable.chain.heytap.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.meowcat.wearable.chain.R;
import org.meowcat.wearable.chain.heytap.activity.AboutActivity;
import org.meowcat.wearable.chain.heytap.activity.AchievementActivity;
import org.meowcat.wearable.chain.heytap.activity.NewGameActivity;
import org.meowcat.wearable.chain.heytap.model.ChessModel;
import org.meowcat.wearable.chain.heytap.util.MyApplication;
import org.meowcat.wearable.chain.heytap.util.SharedPreferencesUtil;

public class MenuFragment extends Fragment implements View.OnClickListener
{
    private static final String ARG_CHESS_MODEL = "argChessModel";

    private Context ctx;
    private View rootLayout;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private ChessModel chessModel;

    private MenuFragmentListener menuFragmentListener;

    private int animSpeed;

    public MenuFragment() {}

    public static MenuFragment newInstance(ChessModel chessModel)
    {
        MenuFragment menuFragment = new MenuFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CHESS_MODEL, chessModel);
        menuFragment.setArguments(bundle);
        return menuFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if(getArguments() != null)
        {
            chessModel = (ChessModel) getArguments().getSerializable(ARG_CHESS_MODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ctx = MyApplication.getContext();
        rootLayout = inflater.inflate(R.layout.fragment_menu, container, false);
        sharedPreferencesUtil = new SharedPreferencesUtil();

        animSpeed = sharedPreferencesUtil.getInt(SharedPreferencesUtil.animSpeed, 1);

        setGameInfo();

        rootLayout.findViewById(R.id.menu_new).setOnClickListener(this);
        rootLayout.findViewById(R.id.menu_speed).setOnClickListener(this);
        rootLayout.findViewById(R.id.menu_achievement).setOnClickListener(this);
        rootLayout.findViewById(R.id.menu_about).setOnClickListener(this);
        rootLayout.findViewById(R.id.menu_exit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().finish();
            }
        });

        return rootLayout;
    }

    private void setGameInfo()
    {
        if(chessModel != null)
        {
            rootLayout.findViewById(R.id.menu_game).setVisibility(View.VISIBLE);
            String color, level = null;
            if(!chessModel.getChessBlood().contains(0))
            {
                if(chessModel.getChessRound() % 2 == 0 ^ chessModel.isChessInitiative()) color = "<font color=\"#2E9BC6\">蓝方</font>操作";
                else color = "<font color=\"#E53831\">红方</font>操作";
            }
            else if(chessModel.getChessBlood().get(0) == 0)
                color = "<font color=\"#E53831\">红方</font>胜";
            else
                color = "<font color=\"#2E9BC6\">蓝方</font>胜";
            if(chessModel.getChessMode() == 0)
                level = "新手";
            else if(chessModel.getChessMode() == 1)
                level = "入门";
            else if(chessModel.getChessMode() == 2)
                level = "熟练";
            else if(chessModel.getChessMode() == 3)
                level = "大师";
            ((TextView) rootLayout.findViewById(R.id.menu_game_info)).setText(Html.fromHtml(String.format(
                    ctx.getResources().getString(R.string.menu_game_info), (chessModel.getChessRound() - 1) / 2 + 1, color,
                    chessModel.getChessMode() == -1 ? "双人游戏" : ("单人游戏，对战AI等级：" + level))));
            if(chessModel.getTutorialProgress() > 0)
            {
                ((TextView) rootLayout.findViewById(R.id.menu_game_mode)).setText(getResources().getString(R.string.menu_game_title_tutorial));
                ((TextView) rootLayout.findViewById(R.id.menu_exit)).setText(getResources().getString(R.string.menu_exit));
            }
            else
            {
                ((TextView) rootLayout.findViewById(R.id.menu_game_mode)).setText(getResources().getString(R.string.menu_game_title));
                ((TextView) rootLayout.findViewById(R.id.menu_exit)).setText(getResources().getString(R.string.menu_exit_save));
            }
        }
        else
        {
            rootLayout.findViewById(R.id.menu_game).setVisibility(View.GONE);
            ((TextView) rootLayout.findViewById(R.id.menu_exit)).setText(getResources().getString(R.string.menu_exit));
        }

        switch (animSpeed)
        {
            case 0:
                ((TextView) rootLayout.findViewById(R.id.menu_speed_speed)).setText("快速");
                break;
            case 1:
                ((TextView) rootLayout.findViewById(R.id.menu_speed_speed)).setText("中速");
                break;
            case 2:
                ((TextView) rootLayout.findViewById(R.id.menu_speed_speed)).setText("慢速");
                break;
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.menu_new)
        {
            Intent intent = new Intent(ctx, NewGameActivity.class);
            startActivityForResult(intent, 0);
        }
        else if(view.getId() == R.id.menu_speed)
        {
            animSpeed = (animSpeed + 1) % 3;
            if(animSpeed < 0) animSpeed = 4 + animSpeed;
            sharedPreferencesUtil.putInt(SharedPreferencesUtil.animSpeed, animSpeed);
            setGameInfo();
        }
        else if(view.getId() == R.id.menu_achievement)
        {
            Intent intent = new Intent(ctx, AchievementActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.menu_about)
        {
            Intent intent = new Intent(ctx, AboutActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            sharedPreferencesUtil.removeValue(SharedPreferencesUtil.game);
            menuFragmentListener.onMenuFragmentString((ChessModel) data.getSerializableExtra("chessModel"));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(ChessModel chessModel)
    {
        this.chessModel = chessModel;
        setGameInfo();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof MenuFragmentListener)
        {
            menuFragmentListener = (MenuFragmentListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement MenuFragmentListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        menuFragmentListener = null;
    }

    public interface MenuFragmentListener
    {
        void onMenuFragmentString(ChessModel chessModel);
    }
}
