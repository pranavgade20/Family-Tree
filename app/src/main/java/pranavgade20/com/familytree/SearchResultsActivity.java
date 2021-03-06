package pranavgade20.com.familytree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import pranavgade20.com.familytree.gedcom4j.model.Individual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class SearchResultsActivity extends AppCompatActivity {

    ArrayList<ListDetails> results = new ArrayList<ListDetails>();
    public void search(String query) {
        Map<String, Individual> indivisuals = gedcom.data.getIndividuals();
        for (Map.Entry<String, Individual> i : indivisuals.entrySet()) {
            if (i.getValue().getFormattedName().toLowerCase().contains(query)) {
                ListDetails listDetails = new ListDetails();
                listDetails.setRelation(i.getValue().getNames().get(0).toString());
                listDetails.setName(i.getValue().getNames().get(0).toString());
                listDetails.setAge(i.getKey().substring(2, 6));
                results.add(listDetails);
            }
        }

        if (results.size() == 0) {
            ListDetails listDetails = new ListDetails();
            listDetails.setRelation("Tap to try again");
            listDetails.setName("No results found");
            listDetails.setAge("");
            results.add(listDetails);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ListView listView = (ListView) findViewById(R.id.search_results);
                listView.setAdapter(new ListBaseAdapter(getApplicationContext(), results));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (results.get(0).getAge().isEmpty() || results.get(0).getAge().equals("")){
                            Intent intent = new Intent(getApplicationContext(), homeActivity.class);
                            startActivity(intent);
                        } else {
                            String id = "@I" + results.get(i).getAge() + "@";
                            Intent intent = new Intent(getApplicationContext(), baseActivity.class);

                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        String searchQuery = "";
        Intent intent = getIntent();
        if (intent.hasExtra("query")){
            searchQuery = intent.getStringExtra("query").toLowerCase();
        }

        search(searchQuery);
    }

    public void onFabPress(View v){
        Intent i = new Intent(getApplicationContext(), homeActivity.class);
        startActivity(i);
    }
}
