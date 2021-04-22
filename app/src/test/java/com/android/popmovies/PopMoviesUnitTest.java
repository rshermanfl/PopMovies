package com.android.popmovies;

//import org.json.JSONObject;
import org.junit.Test;

import okhttp3.Request;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PopMoviesUnitTest {
/*
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
*/

    @Test
    public void test_getPopularRequest() {
        APIHelper api = APIHelper.getInstance();
        Request result = null;
        String eval = "";
        String msg = "";

        try {
            result = api.getMPopularRequest(1);
            if (result != null) {
                eval = result.toString();
            }
            msg = eval;
            System.out.println(msg);
        } catch (Exception err) {
            msg = err.getLocalizedMessage();
        }

        assertTrue(msg, eval.length() > 0);
    }

    @Test
    public void test_getGenreListRequest() {
        APIHelper api = APIHelper.getInstance();
        Request result = null;
        String eval = "";
        String msg = "";

        try {
            result = api.getMGenreRequest();
            if (result != null) {
                eval = result.toString();
            }
            msg = eval;
            System.out.println(msg);
        } catch (Exception err) {
            msg = err.getLocalizedMessage();
        }

        assertTrue(msg, eval.length() > 0);
    }

    @Test
    public void test_getConfigurationRequest() {
        APIHelper api = APIHelper.getInstance();
        Request result = null;
        String eval = "";
        String msg = "";

        try {
            result = api.getMConfigurationRequest();
            if (result != null) {
                eval = result.toString();
            }
            msg = eval;
            System.out.println(msg);
        } catch (Exception err) {
            msg = err.getLocalizedMessage();
        }

        assertTrue(msg, eval.length() > 0);
    }
}