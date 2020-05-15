package org.meowcat.wearable.chain.heytap.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.meowcat.wearable.chain.R;
import org.meowcat.wearable.chain.heytap.model.ChessModel;

public class NewGameActivity extends AppCompatActivity implements View.OnClickListener
{
    Intent returnIntent = new Intent();
    ChessModel chessModel;

    int[][] selectView;
    int[] select = new int[]{0, 0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        setContentView(R.layout.activity_new_game);

        setResult(RESULT_CANCELED, returnIntent);

        selectView = new int[][]{
                {R.id.new_game_mode_1, R.id.new_game_mode_2},
                {R.id.new_game_ai_1, R.id.new_game_ai_2, R.id.new_game_ai_3, R.id.new_game_ai_4},
                {R.id.new_game_initiative_1, R.id.new_game_initiative_2},
                {R.id.new_game_blood_1, R.id.new_game_blood_2, R.id.new_game_blood_3, R.id.new_game_blood_4},
                {R.id.new_game_size_1, R.id.new_game_size_2, R.id.new_game_size_3}};

        for(int[] i : selectView)
            for(int j : i)
                findViewById(j).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        int[] position = contains(view.getId());
        if(position[0] >= 0 && position[1] >= 0)
        {
            for(int i : selectView[position[0]])
                ((TextView) findViewById(i)).setTextColor(getResources().getColor(R.color.gray));
            ((TextView) findViewById(selectView[position[0]][position[1]])).setTextColor(getResources().getColor(R.color.chess_blue));
            select[position[0]] = position[1];
        }
        if(position[0] == 0)
        {
            if(position[1] == 0)
            {
                findViewById(R.id.new_game_ai_title).setVisibility(View.VISIBLE);
                findViewById(R.id.new_game_ai_select).setVisibility(View.VISIBLE);
            }
            else if(position[1] == 1)
            {
                findViewById(R.id.new_game_ai_title).setVisibility(View.GONE);
                findViewById(R.id.new_game_ai_select).setVisibility(View.GONE);
            }
        }
    }

    private int[] contains(int id)
    {
        for(int i = 0; i < selectView.length; i++)
            for(int j = 0; j < selectView[i].length; j++)
                if(selectView[i][j] == id)
                    return new int[]{i, j};
        return new int[]{-1, -1};
    }

    public void clickNewGameStart(View view)
    {
        chessModel = ChessModel.newInstanceFormNewGame(select[3] + 3, select[2] == 0, select[4] * 2 + 3, select[0] == 1 ? -1 : select[1]);
        returnIntent.putExtra("chessModel", chessModel);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
