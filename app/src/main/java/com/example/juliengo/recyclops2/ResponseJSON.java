package com.example.juliengo.recyclops2;

import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.Iterator;

public class ResponseJSON {

    private String rawData;
    private String[] labels;

    public ResponseJSON(String raw) {
        rawData = raw;
        initialise();
    }

    public String[] getLabels() {
        return labels;
    }

    private void initialise() {

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode;
        try {
            //convert json string to object
            rootNode = objectMapper.readTree(rawData);
            ArrayNode resNode = (ArrayNode)rootNode.path("responses");
            ArrayNode labelNode = (ArrayNode)resNode.get(0).path("labelAnnotations");

            Iterator<JsonNode> labelNodes = labelNode.elements();
            int i = 0;
            labels = new String[labelNode.size()];
            while (labelNodes.hasNext()
            ) {
                labels[i] = labelNodes.next().path("description").toString();
                i++;
            }
        } catch (IOException e) {
            labels = new String[] {"error"};
        }

    }

}
