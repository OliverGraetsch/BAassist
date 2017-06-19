package edu.ba.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.ba.baassist.MainActivity;
import edu.ba.baassist.R;
import edu.ba.baassist.connAdapter;

/**
 * Created by richa on 14.06.2017.
 */

public class MainFragment extends Fragment{

    String user= (String) connAdapter.UserGlobal;
    String hash= (String) connAdapter.HashGlobal;
    String useString = user+"."+hash;

    String startT = String.valueOf(connAdapter.StartTime());
    String endT = String.valueOf(connAdapter.EndTime(Long.valueOf(startT))+3024000);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){



        String output="";
        try {
            output= new timetableTask(user,hash,startT,endT)
                    .execute()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        String[] timeTableData = output.split("title");
//                {
//                "INGG",
//                "Linux",
//                "Java",
//                "Elektronik-Analog",
//                "Englisch",
//                "WIARB",
//                "Elektronik-Analog",
//                "RTG",
//                "Mathe",
//                "Elektronik Praktikum"
//
//        };


        for(int i=1; i<timeTableData.length; i++){

            String[] temp=timeTableData[i].split(",");      //Daten-String in verschiedene Atrrays zur Weiterverarbeitung aufteilen

            String subject = temp[0];
            String teacher = temp[9];
            String beginn = temp[1];
            String end=temp[2];


            timeTableData[i]=subject+"\n"+teacher+"\n"+beginn+"-"+end;
            timeTableData[i]=timeTableData[i].replaceAll("[\"\\[\\{\\}\\]]","");        //Sonderzeichen entfernen
        }
        timeTableData[0]="Stundenplan";
        List<String> timeTableList = new ArrayList<>(Arrays.asList(timeTableData));


        ArrayAdapter <String> timetableListeAdapter =
                new ArrayAdapter<>(
                        getActivity(), //aktuelle Umgebung (diese Activity)
                        R.layout.list_item_timetable, // ID der XML-Layout Datei
                        R.id.list_item_timetable_textview, // ID des TextViews
                        timeTableList); // Beispieldaten aus der ArrayList

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView timetableListView = (ListView) rootView.findViewById(R.id.listview_timetable);
        timetableListView.setAdapter(timetableListeAdapter);

        return rootView;
    }

    public class timetableTask extends AsyncTask<Void, Void,String> {

        private String userName;
        private String hashValue;
        private String startTime;
        private String endTime;

        timetableTask(String id, String hash, String start, String end){
            userName=id;
            hashValue=hash;
            startTime=start;
            endTime=end;
        }

        @Override
        protected String doInBackground(Void...params){
            String url = connAdapter.getcal(userName,hashValue, startTime, endTime);
            return url;
        }

    }


}