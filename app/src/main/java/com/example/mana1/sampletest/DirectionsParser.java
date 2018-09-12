package com.example.mana1.sampletest;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectionsParser {

    /**
     * Returns a list of lists containing latitude and longitude from a JSONObject
     */
    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        JSONObject jDistance = null;
        JSONObject jDuration = null;
        JSONObject jDepature = null;
        JSONObject jArrive = null;
        JSONObject jStation_Type = null;

        try {
            jRoutes = jObject.getJSONArray("routes");
            Log.d("routes_length", String.valueOf(jRoutes.length()));
            // Loop for all routes
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();
                List html = new ArrayList<HashMap<String, String>>();

                //Loop for all legs
                for (int j = 0; j < jLegs.length(); j++) {

                    jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
                    HashMap<String, String> hmDistance = new HashMap<String, String>();
                    hmDistance.put("distance", jDistance.getString("text"));

                    /** Getting duration from the json data */
                    jDuration = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
                    HashMap<String, String> hmDuration = new HashMap<String, String>();
                    hmDuration.put("duration", jDuration.getString("text"));
                    /** Adding distance object to the path */
                    path.add(hmDistance);
                    /** Adding duration object to the path */
                    path.add(hmDuration);
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                    //Loop for all steps
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List list = decodePolyline(polyline);

                        HashMap<String, String> hp = new HashMap<String, String>();
                        HashMap<String, String> hp2=new HashMap<>();
                        String html_str = "";
                        String html_mode="";
                        html_str = (String) ((JSONObject) jSteps.get(k)).get("html_instructions");
                        html_mode=(String) ((JSONObject) jSteps.get(k)).get("travel_mode");
                        Log.d("mode",html_mode);
                        if(html_mode.equals("TRANSIT")){
                            jDepature=((JSONObject)((JSONObject)jSteps.get(k)).getJSONObject("transit_details")).getJSONObject("departure_stop");
                            Log.d("departure",jDepature.getString("name"));
                            jArrive=  ((JSONObject)((JSONObject)jSteps.get(k)).getJSONObject("transit_details")).getJSONObject("arrival_stop");
                            jStation_Type = ((JSONObject)((JSONObject)((JSONObject)jSteps.get(k)).getJSONObject("transit_details")).getJSONObject("line")).getJSONObject("vehicle");

                            //判斷要不要加後面的標示
                            Pattern pattern=Pattern.compile("[捷運]");//正規表示法
                            Matcher matcher =pattern.matcher(jDepature.getString("name"));
                            Matcher matcher1 =pattern.matcher(jArrive.getString("name"));

                            if(matcher.find()){
                                hp.put("html_tran_departure","搭乘"+jDepature.getString("name"));
                                if(matcher1.find()){
                                    hp.put("html_tran_arrive","到"+jArrive.getString("name"));
                                }else if(!matcher1.find() && jStation_Type.getString("name").equals("巴士")){
                                    hp.put("html_tran_arrive","到"+jArrive.getString("name")+"(公車站)");
                                }else if(!matcher1.find() && jStation_Type.getString("name").equals("地下鐵")){
                                    hp.put("html_tran_arrive","到"+jArrive.getString("name")+"(捷運)");
                                }
                            }else if(matcher1.find()){
                                hp.put("html_tran_arrive","到"+jArrive.getString("name"));
                                if(matcher.find()){
                                    hp.put("html_tran_departure","搭乘"+jDepature.getString("name"));
                                }else if(!matcher.find() && jStation_Type.getString("name").equals("巴士")){
                                    hp.put("html_tran_departure","搭乘"+jDepature.getString("name")+"(公車站)");
                                }else if(!matcher.find() && jStation_Type.getString("name").equals("地下鐵")){
                                    hp.put("html_tran_departure","搭乘"+jDepature.getString("name")+"(捷運)");
                                }
                            }else if(!matcher.find() && !matcher1.find() && jStation_Type.getString("name").equals("巴士")){
                                hp.put("html_tran_departure","搭乘"+jDepature.getString("name")+"(公車站)");
                                hp.put("html_tran_arrive","到"+jArrive.getString("name")+"(公車站)");
                            }else if(!matcher.find() && !matcher1.find() && jStation_Type.getString("name").equals("地下鐵")){
                                hp.put("html_tran_departure","搭乘"+jDepature.getString("name")+"(捷運)");
                                hp.put("html_tran_arrive","到"+jArrive.getString("name")+"(捷運)");
                            }
                        }else{
                            hp.put("html_str",html_str);
                        }
                        html.add(hp);
                        //Loop for all points
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lon", Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                }
                routes.add(path);
                routes.add(html);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return routes;
    }

    /**
     * Method to decode polyline
     * Source : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private List decodePolyline(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}
