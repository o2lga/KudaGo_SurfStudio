package maxzonov.kudago;

import org.junit.Test;

import maxzonov.kudago.utils.Utility;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilUnitTest {

    @Test
    public void dateConverting_isCorrect() {
        assertEquals(Utility.convertDatesPeriod(1537594791, 1537594819), "22 Сентябрь");
        assertEquals(Utility.convertDatesPeriod(1537508543, 1537594819), "21 - 22 Сентябрь");
        assertEquals(Utility.convertDatesPeriod(1537508543, 1540100543), "21 Сентябрь - 21 Октябрь");
    }
}