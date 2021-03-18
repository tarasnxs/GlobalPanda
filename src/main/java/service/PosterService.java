package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import service.entity.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PosterService {
    private static final String TOKEN = "021478:136217803b2cf1d41eabade39a278fbd";
    ObjectMapper objectMapper;

    public PosterService() {
        objectMapper = new ObjectMapper();
    }

    public List<Product> getProductsList() {
        String response = PosterRequest.sendRequest("menu.getProducts", "json", TOKEN, new HashMap<>());
        ProductResponse result = null;
        try {
            result = objectMapper.readValue(response, ProductResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.response;
    }

    public ArrayList<Sales> getSalesList(Calendar from, Calendar to) {
        String start = calendarToYmd(from);
        String end = calendarToYmd(to);
        HashMap<String, String> params = new HashMap<>();
        params.put("date_from", start);
        params.put("date_to", end);
        String response = PosterRequest.sendRequest("dash.getProductsSales", "json", TOKEN, params);
        SalesResponse result = null;
        try {
            result = objectMapper.readValue(response, SalesResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.response;
    }

    private String calendarToYmd(Calendar cal) {
        return cal.get(Calendar.YEAR) + "" +
                ((cal.get(Calendar.MONTH) + 1) < 10 ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) +
                "" + (cal.get(Calendar.DATE) < 10 ? "0" + cal.get(Calendar.DATE) : cal.get(Calendar.DATE));
    }

}
