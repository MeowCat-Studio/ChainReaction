package org.meowcat.wearable.chain.heytap.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.heytap.wearable.support.widget.HeyToast;

import org.greenrobot.eventbus.EventBus;
import org.meowcat.wearable.chain.R;
import org.meowcat.wearable.chain.heytap.adapter.ChessAdapter;
import org.meowcat.wearable.chain.heytap.game.Ai;
import org.meowcat.wearable.chain.heytap.game.Control;
import org.meowcat.wearable.chain.heytap.model.ChessModel;
import org.meowcat.wearable.chain.heytap.util.MyApplication;
import org.meowcat.wearable.chain.heytap.util.SharedPreferencesUtil;

public class TutorialFragment extends Fragment implements View.OnClickListener
{
    private Context ctx;
    private View rootLayout;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private ChessModel chessModel;
    private Control control;
    private Ai ai;

    private RecyclerView recyclerView;
    private ChessAdapter chessAdapter;

    private ObjectAnimator previewObjectAnimator;

    private Handler handler = new Handler();
    private Runnable runnableRotation;

    private int animSpeed;
    private int player;
    private int select = -1;

    private TutorialFragmentListener tutorialFragmentListener;

    public TutorialFragment() {}

    public static TutorialFragment newInstance()
    {
        return new TutorialFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ctx = MyApplication.getContext();
        rootLayout = inflater.inflate(R.layout.fragment_tutorial, container, false);
        sharedPreferencesUtil = new SharedPreferencesUtil();

        chessModel = ChessModel.newInstanceFormTutorial();
        control = new Control(chessModel, false);
        ai = new Ai(chessModel.getChessMode(), control, chessModel.getChessSize());

        recyclerView = rootLayout.findViewById(R.id.tutorial_chess);
        chessAdapter = new ChessAdapter(chessModel, control, animSpeed);
        GridLayoutManager layoutManager = new GridLayoutManager(ctx, chessModel.getChessSize());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chessAdapter);

        ObjectAnimator oa1 = ObjectAnimator.ofFloat(rootLayout.findViewById(R.id.tutorial_hint_1), "scaleX", 1f, 2f, 1f);
        oa1.setRepeatCount(ValueAnimator.INFINITE);
        oa1.setDuration(2000);
        oa1.start();
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(rootLayout.findViewById(R.id.tutorial_hint_1), "scaleY", 1f, 2f, 1f);
        oa2.setRepeatCount(ValueAnimator.INFINITE);
        oa2.setDuration(2000);
        oa2.start();
        ObjectAnimator oa3 = ObjectAnimator.ofFloat(rootLayout.findViewById(R.id.tutorial_hint_2), "scaleX", 1f, 2f, 1f);
        oa3.setRepeatCount(ValueAnimator.INFINITE);
        oa3.setDuration(2000);
        oa3.start();
        ObjectAnimator oa4 = ObjectAnimator.ofFloat(rootLayout.findViewById(R.id.tutorial_hint_2), "scaleY", 1f, 2f, 1f);
        oa4.setRepeatCount(ValueAnimator.INFINITE);
        oa4.setDuration(2000);
        oa4.start();

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
                {
                    if(control.getNext()[0] == -1)
                    {
                        if(control.getNext()[1] == 0)
                            chessModel.getChessBlood().set(0, chessModel.getChessBlood().get(0) - 1);
                        else if(control.getNext()[1] == 1)
                            chessModel.getChessBlood().set(1, chessModel.getChessBlood().get(1) - 1);
                    }
                    chessModel.setChessRound(chessModel.getChessRound() + 1);
                    chessModel.setTutorialProgress(chessModel.getTutorialProgress() + 1);
                    EventBus.getDefault().post(chessModel);
                    setIcon();
                    next();
                }
            }
        };

        rootLayout.findViewById(R.id.tutorial_tutorial_next).setOnClickListener(this);
        rootLayout.findViewById(R.id.tutorial_control_1_wall).setOnClickListener(this);
        rootLayout.findViewById(R.id.tutorial_control_2_wall).setOnClickListener(this);

        rootLayout.findViewById(R.id.tutorial_select_icon_left).setOnClickListener(this);
        rootLayout.findViewById(R.id.tutorial_select_icon_straight).setOnClickListener(this);
        rootLayout.findViewById(R.id.tutorial_select_icon_right).setOnClickListener(this);

        rootLayout.findViewById(R.id.tutorial_select_button_cancel).setOnClickListener(this);
        rootLayout.findViewById(R.id.tutorial_select_button_ok).setOnClickListener(this);
        rootLayout.findViewById(R.id.tutorial_select).setOnClickListener(this);

        rootLayout.findViewById(R.id.tutorial_select_preview).setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        rootLayout.findViewById(R.id.tutorial_select).animate().cancel();
                        previewObjectAnimator = ObjectAnimator.ofFloat(rootLayout.findViewById(R.id.tutorial_select), "alpha",
                                                                       rootLayout.findViewById(R.id.tutorial_select).getAlpha(), 0f);
                        previewObjectAnimator.setDuration((int) (rootLayout.findViewById(R.id.tutorial_select).getAlpha() * 400)).start();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if((float) previewObjectAnimator.getAnimatedValue() > 0.6)
                            ObjectAnimator.ofFloat(rootLayout.findViewById(R.id.tutorial_select_preview_hint), "alpha", 1f, 1f, 0f).setDuration(1200).start();
                        previewObjectAnimator.cancel();
                        rootLayout.findViewById(R.id.tutorial_select).animate().alpha(1).setDuration(400).start();
                        break;
                }
                return true;
            }
        });

        if(chessModel.getChessMode() == -1)
            rootLayout.findViewById(R.id.tutorial_control_2).setRotation(180);

        setIcon();
        EventBus.getDefault().post(chessModel);

        return rootLayout;
    }

    private void ai()
    {
        if(chessModel.getChessMode() >= 0 && (chessModel.getChessRound() % 2 == 0) == chessModel.isChessInitiative() && !chessModel.getChessBlood().contains(0))
        {
            int select = ai.next();
            control.createTemp();
            if(select != 0)
            {
                control.setNext(new int[]{0, chessModel.getChessSize() / 2});
                control.setAngle(select);
                handler.postDelayed(runnableRotation, 500);
            }
            else
            {
                chessModel.setChessRound(chessModel.getChessRound() + 1);
                setIcon();
                EventBus.getDefault().post(chessModel);
            }
        }
    }

    private void setIcon()
    {
        ((TextView) rootLayout.findViewById(R.id.tutorial_control_1_blood)).setText(String.valueOf(chessModel.getChessBlood().get(0)));
        ((TextView) rootLayout.findViewById(R.id.tutorial_control_2_blood)).setText(String.valueOf(chessModel.getChessBlood().get(1)));

        rootLayout.findViewById(R.id.tutorial_hint_1).setVisibility(View.GONE);
        rootLayout.findViewById(R.id.tutorial_hint_2).setVisibility(View.GONE);

        if(chessModel.getChessBlood().get(0) <= 0)
            HeyToast.showToast(ctx, "红方胜", Toast.LENGTH_SHORT);
        else if(chessModel.getChessBlood().get(1) <= 0)
            HeyToast.showToast(ctx, "蓝方胜", Toast.LENGTH_SHORT);
        else
        {
            if(chessModel.getChessRound() % 2 == 0 ^ chessModel.isChessInitiative())
            {
                rootLayout.findViewById(R.id.tutorial_hint_1).setVisibility(View.VISIBLE);
                rootLayout.findViewById(R.id.tutorial_hint_2).setVisibility(View.GONE);
            }
            else if(chessModel.getChessMode() == -1)
            {
                rootLayout.findViewById(R.id.tutorial_hint_1).setVisibility(View.GONE);
                rootLayout.findViewById(R.id.tutorial_hint_2).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.tutorial_control_1_wall && rootLayout.findViewById(R.id.tutorial_hint_1).getVisibility() == View.VISIBLE)
        {
            player = 0;
            rootLayout.findViewById(R.id.tutorial_select).setVisibility(View.VISIBLE);
            ((TextView) rootLayout.findViewById(R.id.tutorial_select_title)).setText(
                    Html.fromHtml(String.format(
                            getResources().getString(R.string.select_title), "<font color=\"#2E9BC6\">蓝方箭头</font>")));
            ((TextView) rootLayout.findViewById(R.id.tutorial_select_button_hint)).setText(getResources().getString(R.string.select_left));
            rootLayout.findViewById(R.id.tutorial_select_icon_left).setBackgroundResource(R.drawable.shape_bg_select_icon);
                rootLayout.findViewById(R.id.tutorial_select_icon_straight).setBackgroundResource(0);
            rootLayout.findViewById(R.id.tutorial_select_icon_right).setBackgroundResource(0);
            rootLayout.findViewById(R.id.tutorial_select).setRotation(0);
            if(chessModel.getTutorialProgress() == 5)
                rootLayout.findViewById(R.id.tutorial_select_icon_straight).setVisibility(View.GONE);
            else
                rootLayout.findViewById(R.id.tutorial_select_icon_straight).setVisibility(View.VISIBLE);
            preview(player, -1);
        }
        else if(view.getId() == R.id.tutorial_control_2_wall && rootLayout.findViewById(R.id.tutorial_hint_2).getVisibility() == View.VISIBLE)
        {
            player = 1;
            rootLayout.findViewById(R.id.tutorial_select).setVisibility(View.VISIBLE);
            ((TextView) rootLayout.findViewById(R.id.tutorial_select_title)).setText(Html.fromHtml(String.format(
                    getResources().getString(R.string.select_title), "<font color=\"#E53831\">红方箭头</font>")));
            ((TextView) rootLayout.findViewById(R.id.tutorial_select_button_hint)).setText(getResources().getString(R.string.select_left));
            rootLayout.findViewById(R.id.tutorial_select_icon_left).setBackgroundResource(R.drawable.shape_bg_select_icon);
            rootLayout.findViewById(R.id.tutorial_select_icon_straight).setBackgroundResource(0);
            rootLayout.findViewById(R.id.tutorial_select_icon_right).setBackgroundResource(0);
            rootLayout.findViewById(R.id.tutorial_select).setRotation(180);
            preview(player, -1);
        }
        else if(view.getId() == R.id.tutorial_select_icon_left && select != -1)
        {
            preview(player, -1);
        }
        else if(view.getId() == R.id.tutorial_select_icon_straight && select != 0)
        {
            preview(player, 0);
        }
        else if(view.getId() == R.id.tutorial_select_icon_right && select != 1)
        {
            preview(player, 1);
        }
        else if(view.getId() == R.id.tutorial_select_button_cancel || view.getId() == R.id.tutorial_select)
        {
            rootLayout.findViewById(R.id.tutorial_select).setVisibility(View.GONE);
            chessModel.resetPreview();
            chessAdapter.notifyItemRangeChanged(0, chessModel.getChessSize() * chessModel.getChessSize(), false);
        }
        else if(view.getId() == R.id.tutorial_select_button_ok)
        {
            control.createTemp();
            chessModel.resetPreview();
            select(select);
        }
        else if(view.getId() == R.id.tutorial_tutorial_next)
        {
            chessModel.setTutorialProgress(chessModel.getTutorialProgress() + 1);
            next();
        }
    }

    private void preview(int player, int angle)
    {
        control.createTemp();
        chessModel.resetPreview();
        select = angle;
        rootLayout.findViewById(R.id.tutorial_select_icon_left).setBackgroundResource(0);
        rootLayout.findViewById(R.id.tutorial_select_icon_straight).setBackgroundResource(0);
        rootLayout.findViewById(R.id.tutorial_select_icon_right).setBackgroundResource(0);
        if(angle == -1)
        {
            rootLayout.findViewById(R.id.tutorial_select_icon_left).setBackgroundResource(R.drawable.shape_bg_select_icon);
            ((TextView) rootLayout.findViewById(R.id.tutorial_select_button_hint)).setText(getResources().getString(R.string.select_left));
            control.preview(player, angle);
        }
        else if(angle == 0)
        {
            rootLayout.findViewById(R.id.tutorial_select_icon_straight).setBackgroundResource(R.drawable.shape_bg_select_icon);
            ((TextView) rootLayout.findViewById(R.id.tutorial_select_button_hint)).setText(getResources().getString(R.string.select_straight));
            chessModel.resetPreview();
        }
        else if(angle == 1)
        {
            rootLayout.findViewById(R.id.tutorial_select_icon_right).setBackgroundResource(R.drawable.shape_bg_select_icon);
            ((TextView) rootLayout.findViewById(R.id.tutorial_select_button_hint)).setText(getResources().getString(R.string.select_right));
            control.preview(player, angle);
        }
        chessAdapter.notifyItemRangeChanged(0, chessModel.getChessSize() * chessModel.getChessSize(), false);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        animSpeed = sharedPreferencesUtil.getInt(SharedPreferencesUtil.animSpeed, 1);
        if(chessAdapter != null)
            chessAdapter.setAnimSpeed(animSpeed);
    }

    private void select(int angle)
    {
        rootLayout.findViewById(R.id.tutorial_hint_1).setVisibility(View.GONE);
        rootLayout.findViewById(R.id.tutorial_hint_2).setVisibility(View.GONE);
        rootLayout.findViewById(R.id.tutorial_select).setVisibility(View.GONE);
        if(angle != 0)
        {
            if(player == 0)
                control.setNext(new int[]{chessModel.getChessChess().size() - 1, chessModel.getChessChess().size() / 2});
            else
                control.setNext(new int[]{0, chessModel.getChessChess().size() / 2});
            control.setAngle(angle);
            handler.post(runnableRotation);
        }
        else
        {
            chessModel.setChessRound(chessModel.getChessRound() + 1);
            EventBus.getDefault().post(chessModel);
            setIcon();
            ai();
        }
    }

    private void tutorial()
    {
        rootLayout.findViewById(R.id.tutorial_tutorial_lay).setVisibility(View.VISIBLE);
        switch(chessModel.getTutorialProgress())
        {
            case 0:
                ((TextView) rootLayout.findViewById(R.id.tutorial_tutorial_text)).setText(getResources().getString(R.string.tutorial_1));
                break;
            case 1:
                ((TextView) rootLayout.findViewById(R.id.tutorial_tutorial_text)).setText(getResources().getString(R.string.tutorial_2));
                break;
            case 3:
                ((TextView) rootLayout.findViewById(R.id.tutorial_tutorial_text)).setText(getResources().getString(R.string.tutorial_3));
                break;
            case 4:
                ((TextView) rootLayout.findViewById(R.id.tutorial_tutorial_text)).setText(getResources().getString(R.string.tutorial_4));
                break;
            case 6:
                if(chessModel.getChessBlood().get(1) < 3)
                    ((TextView) rootLayout.findViewById(R.id.tutorial_tutorial_text)).setText(getResources().getString(R.string.tutorial_5_1));
                else
                    ((TextView) rootLayout.findViewById(R.id.tutorial_tutorial_text)).setText(getResources().getString(R.string.tutorial_5_2));
                break;
            case 7:
                ((TextView) rootLayout.findViewById(R.id.tutorial_tutorial_text)).setText(getResources().getString(R.string.tutorial_6));
                break;
            case 8:
                ((TextView) rootLayout.findViewById(R.id.tutorial_tutorial_text)).setText(getResources().getString(R.string.tutorial_7));
                break;
        }
    }

    private void next()
    {
        rootLayout.findViewById(R.id.tutorial_tutorial_lay).setVisibility(View.GONE);
        switch (chessModel.getTutorialProgress())
        {
            case 1: case 3: case 4: case 6: case 7: case 8:
                tutorial();
                break;
            case 5:
                setIcon();
                break;
            default:
                ai();
            }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof TutorialFragmentListener)
        {
            tutorialFragmentListener = (TutorialFragmentListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement TutorialFragmentListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        tutorialFragmentListener = null;
    }

    public interface TutorialFragmentListener
    {
        void onTutorialFragmentNew();
    }
}
