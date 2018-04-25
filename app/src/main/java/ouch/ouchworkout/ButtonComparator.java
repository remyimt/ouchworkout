package ouch.ouchworkout;

import android.widget.Button;

import java.util.Comparator;

public class ButtonComparator implements  Comparator<Button> {
    @Override
    public int compare(Button b1, Button b2) {
        return b1.getText().toString().compareTo(b2.getText().toString());
    }
}
