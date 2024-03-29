package com.example.juliengo.recyclops2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Map<String, String> map = new HashMap<>();
    public void initialize(){
        map.put("styrofoam", "black bin - garbage");
        map.put("light bulb", "black bin - garbage");
        map.put("syringe", "black bin - garbage");
        map.put("aluminium", "blue bin - containers");
        map.put("can", "blue bin - containers");
        map.put("bottle", "blue bin - containers");
        map.put("tin", "blue bin - containers");
        map.put("carton", "blue bin - containers");
        map.put("glass", "blue bin - containers");
        map.put("jar", "blue bin - containers");
        map.put("drinkware", "blue bin - containers");
        map.put("cup", "blue bin - containers");
        map.put("paper", "gray bin - paper and bags");
        map.put("newspaper", "gray bin - paper and bags");
        map.put("magazine", "gray bin - paper and bags");
        map.put("book", "gray bin - paper and bags");
        map.put("envelope", "gray bin - paper and bags");
        map.put("cardboard", "gray bin - paper and bags");
        map.put("box", "gray bin - paper and bags");
        map.put("plastic bag", "gray bin - paper and bags");
        map.put("shopping bag", "gray bin - paper and bags");
        map.put("toilet paper", "gray bin - paper and bags");
        map.put("egg carton", "gray bin - paper and bags");
        map.put("food", "green bin - compost");
        map.put("bone", "green bin - compost");
        map.put("soup", "green bin - compost");
        map.put("peel", "green bin - compost");
        map.put("eggshell", "green bin - compost");
        map.put("hair", "green bin - compost");
        map.put("plant", "green bin - compost");
        map.put("flower", "green bin - compost");
        map.put("toothpick", "green bin - compost");
        map.put("ashes", "green bin - compost");
    }

    String urlString = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyBFg4cB4lmKW9TnDRXZLm6-tfYB6onVcRY";

    public void recycle(String[] labels){
        //Toast toast = Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG);
        //toast.show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        String itemtype = "Item type: ";
        String bin = "Should be placed in: ";
        alertDialogBuilder.setTitle("Result");
        for (String label : labels){
            label = label.toLowerCase();
            label = label.replaceAll("\\s+","");
            label = label.replaceAll("\n", "");
            label = label.replaceAll("\"", "");
            label = label.replaceAll("\t", "");
            String result = map.get(label);
            //Log.d("result", result);
            if(result != null){
                itemtype += label;
                bin += result;
                alertDialogBuilder.setMessage(itemtype + "\n" + bin);

                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
                return;
            }
        }
        alertDialogBuilder.setMessage("Could not identify this item.\n" + bin + "garbage");
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initialize();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    String test = "hi";
    private static final int REQUEST_CAPTURE_IMAGE = 100;

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                test = Base64.encodeToString(b, Base64.DEFAULT);
                /*
                Context context = getApplicationContext();
                CharSequence text = encodedImage;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                */
                String body = "{\n" +
                        "  \"requests\": [\n" +
                        "      {\n" +
                        "        \"image\":{\n" +
                        "        \"content\": \"" +test+ "\"\n" +
                        "        },\n" +
                        "        \"features\": [\n" +
                        "          {\n" +
                        "            \"type\": \"LABEL_DETECTION\",\n" +
                        "            \"maxResults\": 10\n" +
                        "          }\n" +
                        "        ]\n" +
                        "      }\n" +
                        "  ]\n" +
                        "}";


                try {
                    String response = new ServerCommunication().execute(urlString,body).get();

                    String[] labels = new ResponseJSON(response).getLabels();

                    recycle(labels);

                } catch (Exception e){

                }
            }
        }
    }
    public void sendMessage(View view){
        openCameraIntent();
    }


    //final TextView textView = (TextView) findViewById(R.id.text);
    /*
    String url = "http://www.google.com";

    String urlString = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyBFg4cB4lmKW9TnDRXZLm6-tfYB6onVcRY";



    String body2 = "{\n" +
            "  \"requests\": [\n" +
            "      {\n" +
            "        \"image\":{\n" +
            "        \"content\": \"/9j/4AAQSkZJRgABAQEAZABkAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wgARCAH0AfQDAREAAhEBAxEB/8QAGwABAAIDAQEAAAAAAAAAAAAAAAECAwQFBgf/xAAZAQEBAQEBAQAAAAAAAAAAAAAAAQMCBAX/2gAMAwEAAhADEAAAAffgQAAAAoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIgkWQSCIlVAAAAAAAAAAAAAAAAAAAAAAAAAABHnt8eXrxh74oSZjLze759+rn2UAAAAAAAAAAAAAAAAAAAAAAAAACE+a/S8Ve+ZkuWSxJ0fN6PZeT1SAAAAAAAAAAAAAAAAAAAAAAAAAAVc/N/peKO+LRkSTKRGxjv7fxeuVAAAAAAAAAAAAAAAAAAAAAAAAAAqnzT6HjaZ5ZIoZYxOuljt7bxemVAAAAAAAAAAAAAAAAAAAAAAAAACOb3z8+9/lvpnaS8tOmTlVc2G30Hx+jLKoAAAAAAAAAAAAAAAAAAAAAAAABGp1PGerHkerz7smMhNlOe09X4fV6THTKAAAAAAAAAAAAAAAAAAAAAAAAAADyG+PD9ni3ExWQZ+bzZp6zxe30mfYAAAAAAAAAAAAAAAAAAAAAAAAABNTrjzu+O13lW8lkrZiW2ene8/p2+eigAAAAAAAAAAAAAAAAAAAAAAABGr1xyPR53XMJrdcX562+e7S4O+NDvLb417fm9W3zoAAAAAAAAAAAAAAAAAAAAAAAANe883XHPBaUc1shby1TD3xqd5ue/Q+f1bHPRQAAAAAAAAAAAAAAAAAAAAAAKpwNMt4ArRLRCgRZW84euL86dbHfIoAAAAAAAAAAAAAAAAAAAAAAiOFplk652JaAmJokEVBeIK9c0siddjHeygAAAAAAAAAAAAAAAAAAAABHL655OmW/LtS0syES1ISalIW5MVrR1x0+8+95vZu8aAAAAAAAAAAAAAAAAAAAAACI813xw7x6tcoTKtCgLEliSCE5uuPmtcvVYenuYehQAAAAAAAAAAAAAAAAAAAACPOd58/rj0U6EJkWgIJBJJWyDh7efzffHp8PR6vH0gAAAAAAAAAAAAAAAAAAAAAcbrPndcdidSEstCFlJBeJK9SE4+uPF7z9Dj6fQY+iQAAAAAAAAAAAAAAAAAAAABHJ7z5/fHT56kgsVIBJMTUmK8jja48Lrn1ePo9BluAAAAAAAAAAAAAAAAAAAAABgvGnplM6kqkkKFWiSZV5pZjvOn3xz+8+95vZ0+NZAAAAAAAAAAAAAAAAAAAAABz7xxtMumtjHZnlrEy0qDJLZFmPrmicnvPzmnHqsPT6bH0AAAAAAAAAAAAAAAAAAAAABHE7z5vefZnU2QZVpJEtKsEE2Y+pjPO95+dr12WvscfQAAAAAAAAAAAAAAAAAAAAABrdc8rXz7MsLBYhEsVeLLIc4qp1xytsNfvn0Xj93Uy3AAAAAAAAAAAAAAAAAAAAAA5fXHD1x7BQGeXEkSzV4yLAczWNPO648iz0OPp9dj6AAAAAAAAAAAAAAAAAAAAAAOL3nwOs/Q1ivMrnKFJcpEQZKIsrZ5XTPlR6DLf2uHoUAAAAAAAAAAAAAAAAAAAAANe8cf0efZ5tUi28SqILFlklMaYu+eTr58XU9B5Pd1cdwoAAAAAAAAAAAAAAAAAAAAI5WmfA0x7cuPqYo2VElTJEqi1lbMV54umXnz0mfo9f5/TMKAAAAAAAAAAAAAAAAAAAAA5HeXn9Mu5LXqYE2pbLUtLchBasd5xXnkaZ8Kz0ePo9d5/UAAAAAAAAAAAAAAAAAAAAABhvPH9Hlzc0RbALc2YmrqiUp1MN51NcdTvjt+X29bH0AAAAAAAAAAAAAAAAAAAAAAcvvPja4dfnqtQZpapWVQyyzF0x2R1OFpn5u8egz39p5/SUAAAAAAAAAAAAAAAAAAAAAaHXHB0x6pisVnliMZNtYzySStbzj654m2PK659B5/V6jzeqaAAAAAAAAAAAAAAAAAAAAASc/vjm6ZbVXjGZBLIqYssxCRVLzzNsePpnv4+j2Hl9cqAAAAAAAAAAAAAAAAAAAAAjh6Z8rrjtRlqsmRarURJNEgiicXbHyfXHU41+i+X1yoAAAAAAAAAAAAAAAAAAAAA4/efO7y6UuWdU65vFZQLAkEWQc7TDi6Y7XO3s/J7pUAAAAAAAAAAAAAAAAAAAAAYbzytsLpadavWeSdZZ0itZIvLJYpZj645+uPN0z7GHq7/m9cwoAAAAAAAAAAAAAAAAAAAADn3jk6ZdIssEkywLEXlCqlbzo6Y8jbHay39b5PZNoAAAAAAAAAAAAAAAAAAAAA5HWfGvPbMhFZCpUEqkUFkRqd5+X382zxt7vye2VAAAAAAAAAAAAAAAAAAAAARpd56XeV1pZr98bPPU89WW0tgWBFizS0x4m/nz57ex8XvlQAAAAAAAAAAAAAAAAAAAAEUvPH1xz1MUrJzVJRJBJIsxdcc7THR1y6/n9fc8/plQAAAAAAAAAAAAAAAAAAAABzLzyO8+xFhUkAgiLLJYokdTV7z8/rjl509r5PZKgAAAAAAAAAAAAAAAAAAAAI53fHP7z3uV6hblZYCFkgsQjqa/WfD289Zp7Xx+6VAAAAAAAAAAAAAAAAAAAAAGneNDXLLLj65tzcksLYEElbMnNtUXnkb+fQ1x2M9fW+L6EqAAAAAAAAAAAAAAAAAAAAAMF552uGadSREkKCSsxBdYsomtplzdcd3Pbv+b1yoAAAAAAAAAAAAAAAAAAAACOV1zye8u1E21LkxBBJdQKWVMPWeht58Lv1fj90qAAAAAAAAAAAAAAAAAAAAANLrPna42SLMk7ySxLeWYLJJJCDDpnxvR5cc69l4foyoAAAAAAAAAAAAAAAAAAAAA1euNHXDLz1FRLllsVlIJUgWVs1O89HXLPxp6Ly+yVAAAAAAAAAAAAAAAAAAAAAGveeZt59jjutWW0BKQStwVSLNfTPQ0xvz36Py+2VAAAAAAAAAAAAAAAAAAAAAGreNPXCJcfUouedXhzZJLErJCK1tMeTt55mnrPF75UAAAAAAAAAAAAAAAAAAAAAYXOhrjA6mG85ue8vNxkLBlXJEVVzTvjna+fV0z2Mt/TeT3SoAAAAAAAAAAAAAAAAAAAACNa88XTLflr1zdZlgsAC8shKWa+melpjk519B5fXKgAAAAAAAAAAAAAAAAAAAADnXnzGmPpeep6UiyzAEkllkqhMPfHL0w1u77Dxe60qgAAAAAAAAAAABBIAAAAAAANO8+c28/a57zc9CQVABIUkph7z5+uFXXpfJ7plUAAAAAAAAAAAAAAAAAAAABVObt55XJz0lAAAQqKsmDrPl6+ffz37OHqlQAAAAAAAAAAAAAAAAAAAAANLvLH1xPPRZAIsrZivOWdWlJNmLrjU0y6Xm9m7zoAAAAAAAAAAAAAAAAAAAAAAKpytcMiXdJRp6Y8j0YaHfN02OdNvPTo53JLNWz06eW8qAAAAAAAAAAAAAAAAAAAAIBUhKmPrnW0ypZBjTU0y5+2WuuFpm5vRzu1Jm5028tehxpZQAAAAAAAAAAAAAAAAAAAAAAAIQQioIiAWLKiaKAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEBQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//xAAtEAABBAEDAwQCAgEFAAAAAAABAAIDERIEIVAQEzEFICJBMDIUMyMkNEJwgP/aAAgBAQABBQL/ALV1PqQiefU5yjr9QV/LnX8qdDWTIa+cLS6sT8w7z7tAf9Xyx8FBUViaWI6aT/d8sfEoPcZHZMYCxFYhdppDtO0LQR/5+W1b8I3TNLmy795ZjDuoahwTtTa02rZHO1we3lZ4GTs9SiEUmBRidWDsQHKtnDf0/Tsl0zGNjby3q7ql7gKM2StZNRliqV7CvSDWn5aWXAO0rXvwaFSpUnBGNpTWNjMUokHKSyiMfJ5wcu2UWGw/FA30e3IeC11KGUSDkpJBGKLz7CwOTWYqlijGCnRlqa4tMUncbyBNCRxkeFfvvoQsBjE17E1wcOPne+d2ND6/G68YZadx2ul7WnjeI4ox1ARHWlSrpSpT7IPNwPybxvqjk23GNtM6n217NX+vcp2jkDncb6izN+mh+XW+n179bI0I/I+m+eN1jA4x4OI2HT69zqARWp7TRULjoWMa/jdY0lQQljl9L6W6oqj1IW63rWRZgQuWgjw46QfHNisLJZLJWrWSv2l2zsCO229HG1rON1UvbZFN3XgKgt1g1UFQpWrVhbI0hS1s4hH8x1+mzmR3G6/xpmYnre3Q37gvU2ly7bl6S0h3GytBYHMWSyWasK1av2UqRG0tLcrSBuHG6yUxsim7sn6t8oLFtHZA31tZJzuhWumMKGokcPTZnST8b6h+umY4S/X0v+KHQ+B7D49SBcY4316Uxw1HGygFgkVrIrIqyrKtX1pUqR8PDLwWkoN43WyFkcU7pJb2zOIkcVl8bVqzazWRVolA2tfI5kfdlI9Le57+N1zSWaeNwlP60KrfbHZV1PsGy1wLomxvXpsZbJxslYW9b18ivksSVgVXSlisVii1dtOhs9ty0hA47WOLWQzl8lrJZG/q1krWSyKyRO2RRcVr5HthEj16e9x1HG6qPuRxaftzKlkshRpfFYhYIdK6F22qb3T2F6fp8ZON1MojDJInoBYhYrFyx6hUem/SlqHsiH8mFabV3LxvqC03lvX6/B9a5pdDg5aMHu8brQSIGUm9LAWQqx7q6fWoH+EMtQM+fGyMDg14CtWU6FshEDKwasGrBqbt03W6oo2pQ5yxo6WFufG6qXtMhmdJ03W34NlsqapiyNn8qO4dSO5xuu/TSj5jr9fhljErXw4nTR/PjZ/GLiBYUkuAE0pWb6zesj1vrYW1SvwT327Tm3cafHzLqVLe/bRVBUB0KkjDlhvpGsz43WuLYtLZP4Nul9LUpqMatoUOr7kvG6ohrYt3dfr8L/EkDJnMhZFJxs4cY2OVUKc5+CxKpyordfJboupNvHpqHytW7lD542Vwa0MDfbatWFsqCwah0pPZkO024CIjxuu/p00rnSfgxWK3HVyd8gTbuN1FYxuaix6a6dXKrkq5Fv8AgcEWPaWNcH8bMzNjWNAVWgEPwbq1M97QZnFQxmR3GyOpu7XeR+vvBV+xzUHb9358bN+rv7CcQJwu61CTYEkg3026jq9warBPxc/jZG5trdE0u5IhIsmruMXcau4woOHUkI7qRu+LSmMs8bNJ2o8nEhZC66ffWljaxVKk5gKMIUT+y7jdZ/QzLIGwWhwxQzVuVn2X7HeJu5i0SFDxxmpFwNitM8fkd4daFnjiLAbSAr8sgyZS07Lk46YUPeXALvxhNe14Q6OKaMVADjxx3U3xTXBw6y6gRGWaV58kFqZJiTq3LvihK1ycMlFHZ4/dEWnRIB7Vk5F8hUunc8mOQExvX8eRyjhwIgF9lyGmkKj02K8cnSpUFQWIXbasGqh/5c//xAAjEQACAgEEAwEBAQEAAAAAAAAAAQIRUAMQEjAgITETQXCA/9oACAEDAQE/Af8AVZalD1Gfoz9Gfoz9GfqyE7y7JfdrLLL20nl2P756X3Lsf3a/BM0/uXkcTiUcTicTgacaeY1PRzORyQ2WJkFmNRWONDRRW0ERy8pUOVj2oooQpkZXlZSofso4saYnvJHsiyMrybdD970UcRRKKHE4kZURleRZd9bRRH0J5BvtociMse+iivKRZB2sdLvntpPHSF3TEaWOkLumUaWOmR7piNPHSW1lllllll73tZKQmaeOltRXXRJCiaeOmLuk0KSNN3jpFl9jJCNKqx0xRsrtlE4Gn6x0xPubORp46XwvtZLbSrHTFHukJGnjtQT7pUWaf3HSP700UUUUNCRov1jpiRXbIRpLHTEy9rL6WyUhTNJ46ZRRRW9ebiTicTT+46ZHumWQfvHTFvaL6plmn9x0h78RR6WS20VjplFdfoaJIo08dMXdPbT+46R72bOTLfTZN7aX0WNY9q6qGiS20qx0hdzRRD7jpi7pHIg/YsbIW9eV+M720l7FjWf3ukjiabrHSK6a8ZDZB+xY2ZZRbLZ7PfTJEkaf0WNkrKrukx+zTgLGyP73SRZGXvHS2s5Fl9UpFkfosaxlFbJllll+LGijTiLGvsooocRxNOWOlvRXWyRD6LGssXbIZBexY5oXYyW2lHHyXnZyP0QpIveTIxILHv2S9Cl4S1KHqHI9kG0czmcj6acMi4jgVtbJQs/Ni0xRKOLPzYtJihWUo4nFHE4o4oopf8uf/8QAJBEAAgIBBAMBAAMBAAAAAAAAAAECEVADEDAxEiAhEzJBcID/2gAIAQIBAT8B/wBVhpNi0EfjE/GJ+MT8Yn4onDxzEOvfX6y6Ide+t/HLoh163trdZeHZF+tbar+ZdGi799WX3MaLou/Vs1Pry8Y2JUWfSyyyxxslGsrGNipFo8i1vYntKNko1k0rOi97LG9rFIsaskqyS40WSdlZBLlsolHHrniVZNU8cueG2pjlzw21Mch80TyNTHRGuaIzUxy2oooooooretqIopmo8ctrL9L4InkamOiSXMkOJPrHIoraiuC9o7aveOgORe98SZ5mpjojQuShI8SeOiVyoT21e8dAbE+WI2anWOgVzRQ4mpjo8VlllnkKR5GqsdAbL5UNk+sdEorkSIo8TUx0dr2svey/WyDLNT6sdAfNDZ9Y6I96K4o7T6xy9LL4UR21Xjoll8lkdp9Y6I+ZbT6x0T4UJFIpFcFEUJGp1jkIsvissiyzVvHIfNEsfWOiPmSEh9Y5ell+tFbogltqP5jls+G/RMUjUV45c6EiXWOgUJnwpHw+D4IsiyfWOi6LvmURI1JfMcj+uaL2cfmOW1HiUVxRiUPrHL1raiivVEWWajxy5LLLExMnG8cudCH0PGrnQiXWPT5ltqPHxfvR4s/NjjXokP4SeQX0r0jCyMEitpRTPzPA8TonLIpil6RnR5o/RH6IlM8z9EPVQ55Wyyy2Wy2X/wAuf//EADIQAAECAwUHBAICAgMAAAAAAAEAEQIhMRASMkFQICIwUWGBkQMzcaFCkrHhUnCAgsH/2gAIAQEABj8C/wBqmD04bxGaldHZe79L3o170S9yJYn+QrpDRDWDtw/B1t7MSqvT+dYN2Ld+EN6JViWflZrFEsUSheMu+rg3rsL7xUlRUQOfJYVRUUEUbwjmhFCXBz1a7HSqg9OFqPRUUoTRP1oqBURUMURi5MhDCGA1f07pDtmqjsCsEf6rBH+i/Mf9FM+YUbscP6owEzd9XliRj9RyVRUWErDEsJU4B3CeBoSOS66r1T1VLKKewycFddT/AIV6LZmNiacTQK66i6vZZcS6fKdPqAhgiaBSm3FZ02nlqxSV89kep4reF1TctOgHRAIC0cIOnvFBs4dO7JyOMHiY8rIJ89OBMV1Xb3qH642/XqpRjwt0gyy06FPw6KlgiWAquWnPyWIW0VRs5KoVQqjyqw+Vl2iX9q8K6c3NGEQ3W62zAZUVNmgVAqBYQg3pwl0ZQo/GnQp+fFKgYErCUXB05zkpALCqKm1ULKyo8qo8rHD+yrCiRXTg2aumEbNNmilbCIQJ5snU+WnQp2k1oVeFBdDoyKmCJacVh+rKBUsy2MrKrLyvxWIdlI/SP+WnBldiAtmdrLZ3WHZPfQcvp0LBOjxd2ZdThVDpxdUNlFTbrZUL8VJu1hhz06H5V17KrOyqrZXa3fKxFQA9dOqzK9fGzRU28JQyZYvpX3ppwEQlEt0bGSy2PxWS/Ffivx8LfPiFShPhQ3X5adB3RHG+LIfkadCiTbVVHlVHBiNncadMKUH1ZRTMflUWGHwqDws+yz77GazTTb4VE/LLTg2amBbSymxVVKosKon3ln3CF06dD8qLjXSWTIHlpw3XTNcCZ1QuvZPle19r2z5WEhU26fae6mu1OnTWVnNUVdn+lMqX8LNf0nlY5DHLTgxzW9XhS2Ii1FIKENnp0JiEnRiYtx3e4VDdczrp25VGExh+iqqkQ9CscSxlYvpVsqFULFCp25CFOYwgHBny075kqcKg2GiDpg/hU04fKIiifjvT/wBULPXTg4eaMIC3Y/KnBCqQBUhWGHyqKnAmO6qNPkFWyp4e5C63vTQkw075kjyXRNlZXg0sLwugwI04SzTvICidUKwx+Fgj8KhZZ+FU7dVOJSi05lWyi9sqYIVQqqqqqjZeSwoQwwd9OvKcMusVjGRsqqBUFtNmYUtP7oTkDZNSKoFhVOC8NQt46dEnbPji6yu8zpzcc2A8p6feCfbmQsS3S+1eirFqEqwquwAzkrEwUy6mWV6AqsKdisVnT+dS52YUwhCvX3KwKUI8p5eVvw3uoKcQRHsgIYYmU1OLtrFFhHhYR/xd/8QALBAAAgIBAwQBBAIDAAMAAAAAAAERITFBUWEQUHGRgaGxweEg8EDR8TBwgP/aAAgBAQABPyH/ANMz3xBmg2dSP7L4kakXhET/AOwX7kSB5imwMWhaharu7LO3bEiBISoggi5kd3yGPMiN4RwHi53EhNWPohJ4NcEk5Fd2zeB6Z2TYes+aB01NDjPTE75bMWMPDFDamFX9JJRQu7O5QDSBshItS08jmWpWG7c2lE0aTHwPS1YvCToqBfBKUmvdlpUo4eRKe4qtiN5/Aqrh6mQzRNBqPYbijYIWR3Q1LyoQWShJad3ajBNITuTzBEEscBxuMKdSvIIRAwJriSEIh4y2ju6tY2EJSbdtsSn4uBonX1Gj6DP6WJf54E0ZGgKM0CMOlyu67hbCHIezySBx4EchWSEFNEUUkWJuZQxlAaNlrldznLy6TcUxy2RCxXTJBggRRYJjbYxKxngbC2U8oTJjUu4oY2ESHNIQovgUhvov4ssykmg02EuqpOyAu3skFMy9yjFMEFQiP5JGGNZgwLRoSzWhOVK7c+dh0fyRTvTI5N5lSI0IExbIOsCPTBAiPV3nIYk1XP8Asmfg8duiewzHlfEHFiGLJCdjLoukCDXRkfAobRJFlFlYXnddugEiyLE8oShD6QGeBHS1oS9h9GynkORYcoyGk0jtzRNIpSL9VQghXW44K3IQZHIuiFA0yfYmmsoVDqS+BP8AAqR3RlNjcpz26hpvKJHmxYwx2HwTQcniLoEMSezFKA0zyJyLwxt44qhikgmLJzra+3JfyLLoRRanj0oA+a9keD4ClsfPsf0kf9GNtYFFJoLw8twbattBrsZw+O3QEJ1+BTiTYE/9Hm1Idr2xCpWQ5cBM5EWTTrJOLS9C/UDbAkH9okzDGxIpPrRWLgRI1LJLE9u++GraGwh40NjBY3YpTLFXkggihjmTWeZIvHTMIU/4yJhO67c5o3IWd74IsNOHwZZ+RO1ghuyMai/uROdvZH/Qjn2Gm69j3pHHBK/oSZaiW1DD7cgUnWVkJKaElQkjKWkNKX1Ilq2JKkjyKCTe0EpawyXHolskkSh9Ctl6EUYXo+uDBCVPWiKVU637cjr5JiZanT9ZFMhumwmZp/IkaMxoLoVkptM0IhWLoqhkJmB9bV5RU0lnt0xLClETrLJusfQX6A47FFoukT3RLYTfC+SJ19jyXsfB7IqbYKtSLyP1T8hSYlUY/Dt29p2TMVE4Jp3NwZk2PPsISPU35G1xTXIo7+xaIVWU/QTppehvGno/rBCdL0INxvIVjGEJKF22Zh2yV6qBPJAkxEkmtRVxDZEC+U40EkqRBqyQ5GiOBXko8ix4JDiaYvgUZVlue3I2mgkyoW0x6gruM6Y4k5X7FGckONRO9/Yn3+pP/ofJexv7BtFtvZMTnwgaJl2JmxO/PbnkvI4S4JbsTahSVIXl7Gk6PkgPISaJIttehvx6IqRPQgDZg27TQbblvgdrX+nbuVEibFURBwOLmSEzP0No7Gf9CG/QWgcWkJCgi9RiY0Y9ZYpW+SyEgvLwxHbp6kkmVl7Ebej4DOmvQodvhnJCIIE3gs2EcnC39Re/oMKi6XQRfXhSCJLXl277ArtDDosjD/k8dGHhrNiJuRJz0+5254pN5wPWvwxpVdNQi8sbRH+4JW69/wAIHQgaE2wLz/AqDnH3O3UecWiidG9BNOK89ZiUNTyiYFZNqHlAmF/Il8ezgRC238DRoxTj1EDd76OEmHmecH24+EmUDVxFtAlVOCmb8FtXoS4+hCFHvokaWr8k/uY0+UG79x6eCnRiuMDs/MpYF21Z877EO1PUhhk/z0Kom4p7u0zZCZPzYu2zm1cRInvgUoFhaXwJzWyBZNQT8nqP9EE3PzEJsS1oIQQNGWkTgaJamaYS3O+RLp1BfyLtsZ6BOY34ISvNPglAlY4+SRPKPXT4QmvPoP8AYCoJ6DTjHqTj/YYOUvJPCX4RQOHLXt1OU29DONm+XWeBLWCOCEV0jVLJjBL4JNRJLG25PqOEmhJETKmdO3TTFv6GoBsShGnU/wCSTZqyCjs86mMEpWxsLtuPLZehkTsWCWY1yO8hKYJ9fPJRXrQxlsvQ1DK9CX6It/qGAZ0onsiCKGEpbiSSOTu1IW4WO22vgKJYrJD0M+ekvYtp9CE/osg8OhBaKUjctjTRx8iKg8jWyPmEnNNw3sLts6S6YkVIh2rs+fYlWf4JLcSaMlwyA+IGiTQ6QyTrMCxUSSVIsdtZapoGWjT4hCFu8KYLHMwftDIbNfDYzqeuPySJ4EyTQ8kDGoHi+6ZiA7NkLHba9ZTkUgiFKgOBS/RC69gnPT5IZYuT4EP+oJJXJLEGTTJGtDM5/Au21Uw6mYKWmRRvsJRl4MV6yuBIQ9y9+kiGQJqvuN8IzoEPgomqtmRwJgXbWig6CNQT0gaxEmtkMf6y2VNwJPvDgHDVG46BHVQ1NMmWSdiZ9beBtaRxwLHbcohlLM+IMXHyyF+KFJkQp15YaFMXycEYljKMvU89JQhmRUlP2P1tbjyLGdlx4C7aljKdBoHGzzkEhYEiRsTqJlN+iX+xuv3JYSyNyGiJktAuf0Gu79nzFb+wKOVjyRiazT7crdNg0q2FIqRYIAqa5E60FyQ6huBo1EvaCxEvB5ZBHQR4Lg0Iy95MX+Y/8GhaWQWTD1XWOlllwIZFO3gaiZRa1EibKFCFjtqGNhiFiLVC3aWP/ImRqRs3GeRji+3jHfKO4TGwv5YJfI9B+CQJQeCQxGHhi3buCfRDJjZadvRI0xGqs2TC1QkNZb9W4Uk11iFoikJtjbBJXGQ1cuMrgg5F4sjRoXrBgknnoLrK6/1gXa76Qxp9D96aw0Ma08FAkiJXJDpFEJy2MiNy4GKZctkiUXybjX3ajZrHhkKjaideQFAihFHJq5rZgShCx3KCGxA4jiG/Kv46O/4glYSI/wDlv//aAAwDAQACAAMAAAAQkSSSSQkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkmyNJ6kkkkkkkkkkkkkkkkkkkkkkkkkkkXx8Ehkkkkkkkkkkkkkkkkkkkkkkkkkkgg47Kkkkkkkkkkkkkkkkkkkkkkkkkkkk8u5D1kkkkkkkkkkkkkkkkkkkkkkkkkki5yyQMkkkkkkkkkkkkkkkkkkkkkkkkkiWPZS1Ukkkkkkkkkkkkkkkkkkkkkkkkkxg1QskkkkkkkkkkkkkkkkkkkkkkkkkkkBEFXwkkkkkkkkkkkkkkkkkkkkkkkkkkLqJIuRkkkkkkkkkkkkkkkkkkkkkkkkkXJF3wTYkkkkkkkkkkkkkkkkkkkkkkkkEVgOg+eJkkkkkkkkkkkkkkkkkkkkkkkAyAF2mu60kkkkkkkkkkkkkkkkkkkkkkm0KWl5q16okkkkkkkkkkkkkkkkkkkkkiJrJA8MChUEkkkkkkkkkkkkkkkkkkkkklSz1JIC6WCkkkkkkkkkkkkkkkkkkkkkiWU7pJtVH7EkkkkkkkkkkkkkkkkkkkkkGoj9hE/BuEkkkkkkkkkkkkkkkkkkkkkiQPUv9ZizqEkkkkkkkkkkkkkkkkkkkkkDCWWUdRRlckkkkkkkkkkkkkkkkkkkkkgbGo7yz9abEkkkkkkkkkkkkkkkkkkkkkXsuiTV7kYYkkkkkkkkkkkkkkkkkkkkkgGKXvrAasHEkkkkkkkkkkkkkkkkkkkkkCxkbUwHm9QkkkkkkkkkkkkkkkkkkkkkgJH0kP0TNJUkkkkkkkkkkkkkkkkkkkkkHCIwZIDS/wAtJJJJJJJJJJJJJJJJJJJJIpvOKAxif+JpJJJJJJJJJJJJJJJJJJJJAaw8LARC3QJJJJJJJJJJJJJJJJJJJJJIEOxp9BucSRJJJJJJJJJJJJJJJJJJJJJA6RV1oE+Z2ZJJJJJJJJJJJJJJJJJJJJIAl5fhiY2bdJJJJJJJJJJJJJJJJJJJJJHdHjqetZRdZJJJJJJJJJJJJJJJJJJJJIpLP5NofGrbJJJJJJJJJJJJJJJJJJJJJBCwHhzzDuXZJJJJJJJJJJJJJJJJJJJJICJqyze1vEJpJJJJJJJJJJJJJJJJJJJJA6RLsXsJHV5JJJJJJJJJJJJJJJJJJJJIPVqQSPUUn7JJJJJJJJJJJJJJJJJJJJJFfdbz3wAXRJJJJJJJJJJJJJJJJJJJJJJvpYihqJrmLJJJJJJJJJJJJJJJJJJJJJAqIFBPsnYJZJJJJJJJJJJJJJJJJJJJJIgfcUnvrwipJJJJJJJJJJJJJJJJJJJJJBhZ/M34hflJJJJJJJJJJJJJJJJJJJJJIDgMdnGpdfqJJJJJJJJJJJJJJJJJJJJJFCxum3C8PVZJJJJJJJJJJJJJJJJJJJJIOSi8LP8AdWoSSSSSSSSSSSSSSSSSSSSSSSmen9po1gmSSSSSSSSSSSSSSSSSSSSSSRKLbbOc1ISSSSSSSSSSSSSSSSSSSSSSKnQF9J0AmCSSSSSSSSSSSSSSSSSSSSSRrS9/YEOJpSSSSSSSSSSSSSSSSSSSSSTfpDwUk946mSSSSSSSSSSSSSSSSSSSSSRNyiQkwB3gaSSSSSSSSSSSSSCSSSSSSSUhNAG2iPqjSSSSSSSSSSSSSSSSSSSSSR0xYAmYyDOySSSSSSSSSSSSSSSSSSSSSCt5UG4B0TCSSSSSSSSSSSSSSSSSSSSSASOzNbSVhWSSSSSSSSSSSSSSSSSSSSACfg2rRm7hUSSSSSSSSSSSSSSSSSSSSSSSAUcTL+cySSSSSSSSSSSSSSSSSSSSSSSSSASSSSbSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSf/8QAIREBAQEAAwEBAQADAQEAAAAAAQARECFQMSBBMFFhcID/2gAIAQMBAT8Q/wDGNttLS32RWSvk/jfP6WH1/iXa38ht3R63zfa238BDIfPW+LOpeG2sMED13Yl7PKxYhSaeul0bYsRAx24+jfYzTq6IcqC1bqZ65R52+yEGTf8AMklGMergmvbcwKSMbCV+ThnLN6m2oMLJjueZeKo+SqAR6Cw2dIyzjZeNttkHgzFW2nntu5B3Z+M/G8ZMw6u/I7nzX1J3sTxllnJlllnByMu+gj6/g/OfhlHb545fg/wt9cB35xj+D/AswjbDPOUvwftt5K7+doR0zwMxAsWLFtsxScwQbAzfOXUmxGZYQFhIQF1Mk5YY7Wadea3xK2XnY/Kyw594Qol5rDScN3ttttttt5bLrOffd/JHmrq+q6ckxJZbbLbIZEsDY685ZdF/LbbeBtt4eV6jGxdnzX5Gf6Q9WRwcbxllkkTId/YxZ4859WncnXB+ttlltLGxMQfOHVn1wxwP6ZZTZwNp15rDqWd2cZZZP4GMeP2Q9PO+bXg9R8538sszIDYMeb8WHFdiEX8bbbLY81t5y6jRHICzhllljY2LdHF0mkPN+b4ks4HnPz1J1xAQIjzT1x2zqy6YD8bbbbeMs4W7kR7D55o0ujDK5OnuAss4PwlrJNjsL5y6u10mCzjLLPyBOrM4PGHrzfmUu2Rzln5Zh1LjKHzzTpMH/c58hYgWH9bkmGfvVqfHm5y1vVjd7MjnfxnB5bMY/wBed82LrjLrjLIPwsu8XdglPnmN8WIlhtLrk/Dw8Loyt1nmnq+ps14B+dSxM7nU53LPjzfm3hkjxvO3VnDxvOIlDp5vzA+2l02WWWWNnAJurq+Y7efHmvqC9Sv5AvAPAba2222zk2xKO8D683NBUlkEFnDwTZwvClZbPcMPNXU7wznbZ4LJmS6JA2oyOwfN+JO5wcSnA5OclCMLC3yy+PNPUO5iP5djwP4AeGUjvyfYxMsMPNeF9ZJY7sssssssmMZtEdxDkI+b8Q427doxzn4OGZ31Enz5vzOGUP8AjeJ1toMPNTS7uB/h22V8292zsed/aI/GyJJMI+cBJSLJFszzx8QVYw73xvEZtsrku5e9wIDJ/Eh3Bh5zZaHGI6lSUmjSFj/Lf+ra2v7o4GellixYSHg/4Wf9cQZZZ/8ALP8A/8QAIREAAwACAwEBAQEBAQAAAAAAAAERECEgMVBBMFFhcID/2gAIAQIBAT8QxCEIQhCE/wCHwhPaS0R7EX+B/gf4Df0XX10tnWPvExBITsP1ux0cJh9CbMffrdioHxXQzP31WIooiIeyF3hiRFoGt+s0LoPQmNYo3UMUjd9eg1CZRvRYRR3PYCugm+FFosRdYj39WghmBIhOGkx6wthCFHp1CiGxRPBGpRQRErFoNb0VsSI7JBFLwSKMULSDj0Iqi6L+VEErZqHp+bRNl1C/MUo8UpSlKJjIhPdShBLQmheYxRo+F/KCWLZ08++J+KuEUOB75rO35zE8JjoQ0854zvgqKK4EEEjcYhwGWecuyzBClY2yhNjZSlFRtjMcrYqavmsXAoJYZOEIQSIx4w1jznaY6WxqhLJCEJlMQTpY+hnp5ybIGxRvClKNkIQghyQ2Gqvnb4+mZh80GtDc1U85mnoVqsh0JYZCEzRMYc10NtIp+aYmz4rBKNwTzR5gkNaFY9MZPznYYSEQmbiZSIxg5kpTzm2JaGUTKNoeLwCgQmKkPt859kx0JEIQhCcEJIVMgb+a+j7HWC0QnB8EirEoaMeKeahdlmFwgqLko2ioTFkVXmiO2KzF+GhS4M7vOfYg1hP2NiEITFKKC7HdHWxJhfLY8Z8GiITobsbZSjzoTRBA4TUOyeYlY4NilKXFG+HQg2FvnO534UpS4nBLY0YnVg++a2yB/wAH9hm/hg1ySFsJYlIRIffMeEJbIINDKPgyiCQSdB/jzu4ubilKXkx1gtbzWdhRoSJiEJwohFmaBY4/NfY9ohpIfE7yPJg0uhAffmM7HwYuJwbLwJjzQOQ6862yNm1mlxSlE8fRaOFZfmpWNkfBjT4GhOBCcIMS0Oi1vzaKN0EQY2X8EqKfZA6nnLsQuy5g1ySK1g21o7DPvmPFdCp53g7zSbHUTzQsaj781hdCYmRMafCMorCYgkMhKFroitje/MYlZ0hMQy4pSlLkWWy0ffm9xrQkWDdxfzmjnZ+b2EtDQ/zQtNR5Q3X5qcLIY/ygkPsXRrnn7IPDWYJhO+CaM7CxDYPBZ+etbzHl+CT8I0dgNfg2o4KK6H5yxAQNpiSIhQXdkENaZKGHwDmN30qysoorhRX9KUv/AJZ//8QAKxABAAICAQMDBQEAAwADAAAAAQARITFBUWFxUIGREKGxwdHwQOHxIHCA/9oACAEBAAE/EP8A4V/9IupcuXKSyXLl+rriKAHDz0FbjV/1Nlmz/wBu1OR7Q/ku2qdJP2eUfyS7vHD/ACVDVJkWdQh6trGs5VONquZhwS9TJ71B4fQXK5hnuio9rqHq18G6gq/Uw4dzDWM6lPeAhAKnV8S7F/aWQB5Fyi7gwMrFjCYEZp6sqXNbVuGuepVU8W/mElLhIz8zO4FklyulpvmFRAcCDtFHGGWMoKbMMalXQbAYt3NPVm5ZoNEsQvu0RBUXimr/ALDwptmszcW84thiEQmDpXWe0OUgmHyiijdhIaWFUsmFhz3K2B5PVqEYhxTVnMHCHCGZoFdK+8TMdGaXMa5NoNwwcyVoK3u19odtDetItCE5MD7MQSotfIeZh4petPTrSTiPuYerMKBy9dCld+Jchphzr9oRcA4NGucwgsRmwP7hmcOwQlbSFrE4Ck94Km5TkC69oPqtypDXv13e0tA6s7fHBMKpjAFpSEO1f1HJDR634DAS28F+pszUrL+0BLEE0/HSVhDdh7n0PUmM3S871e0V0KyJ1QO7C2TjWZdnz1cbEwbJY/DwxywlspztlW7XiyoyLKu5SGv/AEBL9RUgfO2TlRPtoDQdDtAmAOwmAcRtOLGb0BDKQcWagKqpsHiDFYSZ2WYC05brMe2CWrcs5rO4PMK6kB9Pcbjc0FrFTaDrDeK/cNPaAbwQNDMXiYb3DX0up0ktC6xzDbXd/aOGqOKEbVmxnwdISXDs6PT07mYFwTNVRljS+JQOMzOPPmBbu0zGWgtzAZqV2lRQ3L2wKfMoJzbITgxtJqCt7rPR88RnSucma/pCJBEsTmHpokgymy9vgZexfCWxYY6cy1VLNYD0gVpUqrAJqvEttOJbBlm53YEd3AkUIM2Y6wk4WGyqgUI999b9xuXK2s8lk/ntD0xIlrj3BQ/svwwmXB4+Zk7ITSGspcGlMyUatg+g2AcwAW4pUXJC0ZWXUMEMWCOEZZOwJ7j9/MMHpjEyxAV7sGLjKoZ8xxGaQ2MwGdRqtEC6bnMHzKhRtDrIlMy8OZX1iYePJmUIAXafEFsRxdhV34zDRD0th3RabTWSjrllPkOU/gsJEAKtWvvFTmp5KmB3gG+IBFViI6y+0QuRg3pqD8B1UsxkMTCExzmVIqt1h22+8UJz0DCCawgpw3CHpbGRYFR1xEAlFU8RJkniB0VClG1RqBfey4Fq0e0KCCRnKGdhZ0h+YfFnvLxknvHFZOSV1wBo3Ob8I8t7xLCgVujnuwDlyy95jE49NZWBtbTvcVFjWh3HMz6BAJyrxFDePdSCDs6XqKVEVLAXQvEVVh5agnJQVeJGGIb4Qc3CMK1bSIWtbzL/AFLdRqBCvzEoVrXQdfv3ho9MZ1yBAJE3dF/aCDFvXKCGq04lSzVqK6lcMVlvcNhRXSPzVtShQCFHl1VAsuxgNEEfomSPi3rhVwWA6y37BdkUdoHQWMiBCgCUW4KzntDXpjqFRKyH8Rqw2wKroR4Ne7GDzTzMO5VRCBaoCitzNBC61Tw4ggMOpazxBQGtSpcAQUYGC74glmkr61uHFlRYrzmN1CWqDBDXpjCkKW65SgbzTa+8NTDsEXeC7iGMUOwlqp7LlWbPfU6K35gHEb4jzSYZYS5dhDsmeB4w11QLF/VjBZCRyEfELoYOFzUCAt4BRR4nHpjqFXCTOsEvGgyv9lcwGCZVArpCBi2yktGKVdwyaRcwBm6RbNTOexywXg9BIVYX5hMTzg3EAIqHhzFV5TrDbU+2DA26sAqsY8xmwDtsS9VAUK4VDXpnEZUeNh2I3caWI4YpplnJ7oAco0+esEAVqAvSvo3DdvLrW4YALrEU20Jvmx0lWutTAtTUsfEF03iDRxg3WpawaTFmJnGGkPTHTFMlZKtGWYYIU1R8wANL6VilxB1BKWgzqqqMVjnDzKKsV9HUUCmDhljv8xGzyRfYoDrPBpQPmKlhOcwdd5tThjZovzBIfslfaV7LS7KWmP3DXpjMQ6ejyBD12mmVmoDmC2sXBQaouKLWmqgO2EMwYBsVm0oA5LxSL6ERkB+8U28NStYc4jhvgi1o1yNwEq5cVlXwFgWfad0G2jEc3WQlOn+emnUL8DYOUxDQgGzN3cbktpDOiBB1uW+jMJQubiCM05oIZfeEMoMQw0QKg2KhmI5mCqfiKpVQZWHsuZRAay2cwQ3e5wQGI0VR8wh6U6nDKiXZjv2hVW2rAKjqc7xU0qq0jUFdO9havtw/6RsFu/3pEVnXrcvonmM4j3yK9PE1RZyyUWhveX6gF75b9xLwJdWK94salpxgmYxFh0oP194ah6WxEKFfAYnGiaP/AEQo04mutxSUroxiFz6qxCunTdxmxd7M1tr5ZYyujcwAo5YDi4VivYhQuPial+0ALgFER/3+o1HHVMPDgc+UPTGFnIZqcVTMDpWSOe8BqjxWbl/9gsgFiKc5ZjVKNhploqa7v5M7I72P1FGleIhb7gDKpKpuIbV8JgssmLDcRh+BX5jwQs0MtdHtKa2S9FJkosqplEhr0ziJEAKHBV/mYxjlcPzFZbPXC/vLRwrvn9xan3C/sNiPqbvzBmRvYf7DJgp8y/FRf6w/7BGl0bxYswVk4ohsV9zMSqTsn8w4atALi4tU7UWXuXBCgtZzD0ziCyhah+JoCII1yl4czmzczhczZmahLZxtmgmaczDziCgm8FSpOM5gwAK1BqHpfErRhqt4mbCoGyqg2LH4mzvNwm+AllKcZpKFvS1/s4x/AfoEqCcRKvWasoGOIylVTb1YFa16mEAALz64w1DXplDQuiXTLS5N6BMlZ6ANSp5DwlMyeGzFPiBhpNF06cJUyx9IC1PuTTRTwisEdVgywPYhXO6AKhBVjkqMUT9jHZmZFnzUHzHpv8Q8wjKwt4a7VD0xaiUKZW1QFyhi4pavdhgG++BwB7JbXl72hmr3y1lRw9pXQ/Eov+IDeLPaYc17BBe3y6E2WmPQx3SBOl9HMSZFX3LIXVlQpcqpiUtI+mmV2rMnyjJBoC/aVonE/eJIbW5b6ENx3NwKuJKC4NlLpNRiBocMosXM+DE09MSyCmpsLoydJuQLDL37ylNhQ4X7x+YbkPhrModk4SL6g9EGXLa+zQ35ZP3FbX2/s6i/EBNME5qUpR1Cb4S1illHzBpmYoCoiEpzaXgwQNhhND01mwFMrBL13SVRxvtDLt3E0lB0SaAx2Vp94OgBrklkz8Evv8IZ4uFhj3I0a14H5ZrHfA/qQqw2dSaAvBIMTjt/OfOwgPZIg9Dp1FIpGb3P4hD0y0h1UNgmcrxt7guOJWbNckyase8W9p1qPUQ8T/FShqGevzMWCPC4gcfD5fMu0r4gNWvfMyUUvE1om8R1yDQc/aIyoVrk5h6apzFQvlmpzMVU48RC4eYbQ3DKpH3ia2/Mvv8AQxPmO5nNHVi1gxNcR4YwclkaOylKC8EO1N9dBZgqaHpjKoiQ4KHWWOF40Hz13CiFsY3XuRwcfkL+YNpHhv8AEr+8p+oJ1TuSBMu/71mmvguX5P8AYA2g6f8AuXDkNG7+8q2vLZR8zDsxVkd94gIGrCNd93suCjTlTuv6B6Za6qqFoVjIM4FENix0YUqcdH8iAvXowAbR7TJseY1Ez2UWoBfDA0i0qj3gukeEMyjAVrmLYBzEtougiOgq9L9qhRKOHHuVWAElZNX+PTgWRYdeGFR9wuYv9UBV/wBEpEC50FxBNTtFwW3LrUU6xvAjNMRyMs9Ae1xzhS7yi38TUjcaSdb3sriWS4TvdpWPTR1DewoUHNPDuaYfW/7AP5gTf95fvCKBis1mct+CHsoh+SYHNv8AwlPEu15bq+ZblTsMp1qXeGzmI7Vy7oioDe7OIlnmoNPiIKsl2LV6uPD016qwE61xK482l7OsSncs/wBiG0vbf6lvTvhvFDl5iVpz4iK6TuE8iYENVi3mXBw54BYxrXRhwAexBqpc3weIOgWFVD8TTilyaHUCg9NK3edgpLKxGiVvkgaJQydRmJMMAa7JOQHUgnVK4ZgwPvNqrFOrKBbg6rRGOK7SgwQa6Yh3CnZIgW1hFCvrKWF1dA/MpZhCUaPDND01KQUkX7k0lIDIfMM6i3csFbH5RvDW54gORwOa/mKGk4J9pwgrrX8yrbfEYgTgt6sHjj6IGqYl2GICwGFL0wczMilxgDxCzGM2DDczHptqGTYnWNnC4yYe/WIsoR5LlimXDYPmpiXqApEbK9Fy8wPWBtN8weXHw/yPU9OKz94EBugyhTrU2il9pS2y5C5rL8IBdyAzPKLvAMFrLNLysxCHpfEb9RAHV1K2NgDJ6FR6dujKDOJgeEQsg4ivGXYRRytMUz+IcfyzxfEyyLfBAUUnIZhsqjrCC6niOEr2hGdey3M0HXCUug6qPgmdtqtYBcZ/2oUm8emkXgz8xV2E3qZ5CEEtZIxauBfvEgUDG0VmD8Sro9RIkanuQfb8mZdvMx/9qAlYHaCIrLxMmNWGlTTxCFyBENrrEyQNGGuMYxzEqJShZ/zLVhp/4K1VoCeG5eyWocSlQiGIfTSOSoi10iuszLjTZFWafEt1gtlxaA3iMNKE5d7j4QaBXMIAOPTScsESPSBVFcwUaTZDmX0hqc7+l/So4+tCDqZwmS88Q2KHAwktjBUvXR+305geZaKHff8AukwBcIVy5mUxQ28Q7Zn3cRGqU8FJ1xAjqIE3iC2SUGhhB1FJHs0206rlhHmU8cQ+M+8PTLOsuCVhKYwjrD6IE7C6wjUvEseAXxFQa6DuM+dCDFGQcNwdcjd7jLTzKWEJsNGIvvqUhwKF774ZRFxxgwSCa9veU91dr5Gg7Jp6VTUqEYwXpSIOrKRclyi+O5H1ZOgzvM1y1mvzEdfAp+oYSuFePBDBcMDA6DBMegh9mESByCwVULhK8Ea2iwBfhuPWgoXDzAtQuze+ZW47pDWZ7vFDAAGADUCvTn6USnJOwimyKOS+0WrH4g1eSEy3k60mqsesZpl4JStSs+rP/wCJP//Z\"\n" +
            "        },\n" +
            "        \"features\": [\n" +
            "          {\n" +
            "            \"type\": \"LABEL_DETECTION\",\n" +
            "            \"maxResults\": 10\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "  ]\n" +
            "}";
    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String body = "{\n" +
                "  \"requests\": [\n" +
                "      {\n" +
                "        \"image\":{\n" +
                "        \"content\": \"" +test+ "\"\n" +
                "        },\n" +
                "        \"features\": [\n" +
                "          {\n" +
                "            \"type\": \"LABEL_DETECTION\",\n" +
                "            \"maxResults\": 10\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "  ]\n" +
                "}";


        try {
            String response = new ServerCommunication().execute(urlString,body).get();

            Toast toast = Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG);
            toast.show();
        } catch (Exception e){

        }
    }*/
    //RequestQueue queue = Volley.newRequestQueue(this);

}

