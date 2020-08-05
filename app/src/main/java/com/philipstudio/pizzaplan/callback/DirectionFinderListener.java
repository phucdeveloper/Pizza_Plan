package com.philipstudio.pizzaplan.callback;

import com.philipstudio.pizzaplan.model.Route;

import java.util.List;

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
