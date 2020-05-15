package org.meowcat.wearable.chain.heytap.model;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import lombok.Data;

/**
 * 被 luern0313 创建于 2020/5/4.
 */
@Data
public class ChessModel implements Serializable
{
    private ArrayList<ArrayList<Integer>> chessChess;
    private ArrayList<ArrayList<Boolean>> chessChessPreview;
    private ArrayList<Integer> chessBlood;
    private boolean chessInitiative;
    private int chessRound;
    private int chessSize;
    private int chessMode;
    private int tutorialProgress = -1;

    private ChessModel() {}

    public static ChessModel newInstanceFormNewGame(final int blood, boolean initiative, int size, int mode)
    {
        ChessModel chessModel = new ChessModel();
        chessModel.chessChess = new ArrayList<>();
        chessModel.chessChessPreview = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++)
        {
            ArrayList<Integer> c = new ArrayList<>();
            ArrayList<Boolean> p = new ArrayList<>();
            for (int j = 0; j < size; j++)
            {
                c.add(random.nextInt(4));
                p.add(false);
            }
            chessModel.chessChess.add(c);
            chessModel.chessChessPreview.add(p);
        }

        chessModel.chessBlood = new ArrayList<Integer>()
        {{
            add(blood);
            add(blood);
        }};
        chessModel.chessInitiative = initiative;
        chessModel.chessRound = 1;
        chessModel.chessSize = size;
        chessModel.chessMode = mode;
        return chessModel;
    }

    public static ChessModel newInstanceFormTutorial()
    {
        ChessModel chessModel = new ChessModel();
        chessModel.tutorialProgress = 0;
        chessModel.chessChess = new ArrayList<ArrayList<Integer>>()
        {{
            add(new ArrayList<>(Arrays.asList(1, 3, 2)));
            add(new ArrayList<>(Arrays.asList(3, 2, 0)));
            add(new ArrayList<>(Arrays.asList(2, 1, 3)));
        }};
        chessModel.chessChessPreview = new ArrayList<ArrayList<Boolean>>()
        {{
            add(new ArrayList<>(Arrays.asList(false, false, false)));
            add(new ArrayList<>(Arrays.asList(false, false, false)));
            add(new ArrayList<>(Arrays.asList(false, false, false)));
        }};
        chessModel.chessBlood = new ArrayList<>(Arrays.asList(3, 3));
        chessModel.chessInitiative = false;
        chessModel.chessRound = 1;
        chessModel.chessSize = 3;
        chessModel.chessMode = 2;
        return chessModel;
    }

    public static ChessModel newInstanceFormJSON(String chessJSON)
    {
        ChessModel chessModel = new ChessModel();
        try
        {
            JSONObject chess = new JSONObject(chessJSON);
            JSONArray chess_chess = chess.getJSONArray("chess");
            chessModel.chessChess = new ArrayList<>();
            chessModel.chessChessPreview = new ArrayList<>();
            for (int i = 0; i < chess_chess.length(); i++)
            {
                ArrayList<Integer> c = new ArrayList<>();
                ArrayList<Boolean> p = new ArrayList<>();
                for (int j = 0; j < chess_chess.getJSONArray(i).length(); j++)
                {
                    c.add(chess_chess.getJSONArray(i).getInt(j));
                    p.add(false);
                }
                chessModel.chessChess.add(c);
                chessModel.chessChessPreview.add(p);
            }

            JSONObject chess_game = chess.getJSONObject("game");
            JSONArray chess_game_blood = chess_game.getJSONArray("blood");
            chessModel.chessBlood = new ArrayList<>();
            for (int i = 0; i < chess_game_blood.length(); i++)
                chessModel.chessBlood.add(chess_game_blood.getInt(i));

            chessModel.chessInitiative = chess_game.getBoolean("initiative");
            chessModel.chessRound = chess_game.getInt("round");
            chessModel.chessSize = chess_game.getInt("size");
            chessModel.chessMode = chess_game.getInt("mode");

        }
        catch (JSONException | NullPointerException e)
        {
            e.printStackTrace();
        }
        return chessModel;
    }

    public void resetPreview()
    {
        chessChessPreview.clear();
        for (int i = 0; i < chessSize; i++)
        {
            ArrayList<Boolean> p = new ArrayList<>();
            for (int j = 0; j < chessSize; j++)
                p.add(false);
            chessChessPreview.add(p);
        }
    }

    @NonNull
    @Override
    public String toString()
    {
        JSONObject chess_json = new JSONObject();
        try
        {
            JSONArray chess_chess_json = new JSONArray();
            for (int i = 0; i < chessChess.size(); i++)
            {
                JSONArray c = new JSONArray();
                for (int j = 0; j < chessChess.get(i).size(); j++)
                    c.put(chessChess.get(i).get(j));
                chess_chess_json.put(c);
            }
            chess_json.put("chess", chess_chess_json);

            JSONObject chess_game = new JSONObject();
            JSONArray chess_game_blood = new JSONArray();
            for (int i = 0; i < chessBlood.size(); i++)
                chess_game_blood.put(chessBlood.get(i));
            chess_game.put("blood", chess_game_blood);

            chess_game.put("initiative", chessInitiative);
            chess_game.put("round", chessRound);
            chess_game.put("size", chessSize);
            chess_game.put("mode", chessMode);
            chess_json.put("game", chess_game);
        }
        catch (JSONException | RuntimeException e)
        {
            e.printStackTrace();
        }
        return chess_json.toString();
    }
}
