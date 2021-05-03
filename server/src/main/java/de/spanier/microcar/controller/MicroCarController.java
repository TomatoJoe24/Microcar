package de.spanier.microcar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import de.spanier.microcar.client.RestClientToMicrocontroller;
import de.spanier.old.database.Cluster;
import de.spanier.old.database.SQLiteConnector;
import de.spanier.old.geoCoder.HereGeoCoder;
import de.spanier.old.interfaces.DBConnector;
import de.spanier.old.interfaces.GeoCoder;
import de.spanier.old.model.GeoData;
import de.spanier.old.model.GeoDataString;

@RestController
    public class MicroCarController {

    private RestClientToMicrocontroller client;

    public MicroCarController() {
        client = new RestClientToMicrocontroller();
    }

    /**
     * REST-PUT Aufruf, um aktuelle Lenker-Position zu setzen
     */
    @RequestMapping(value = "position", consumes = "application/json")
    public void putPosition(@RequestBody int position) {
        //TODO implement
        client.putPosition(position);
    }

    @GetMapping("/tmp")
    public String getTmp() {
        return "Test";
    }


    @PutMapping(value = "forward", consumes = "application/json")
    public void putForward(@RequestBody boolean forward) {
        //TODO implement
        client.putForward(forward);
    }


}
