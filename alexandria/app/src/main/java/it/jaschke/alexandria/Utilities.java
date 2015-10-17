package it.jaschke.alexandria;


import android.widget.TextView;

public final class Utilities {

    /**
     * This method manipulates the textview passed into it and should only be called on the UI thread
     * @param authorsTextView - Textview to contain author information
     * @param commaSeparatedAuthors - A comma separated string containing author information
     */
    public static void formatAuthorTextView(TextView authorsTextView, String commaSeparatedAuthors){

        if(commaSeparatedAuthors == null || commaSeparatedAuthors.isEmpty()){
            authorsTextView.setLines(1);
            authorsTextView.setText("Unknown");
        } else {
            String[] authorsArray;
            authorsArray = commaSeparatedAuthors.split(",");
            authorsTextView.setLines(authorsArray.length);
            authorsTextView.setText(commaSeparatedAuthors.replace(",","\n"));
        }

    }

}
