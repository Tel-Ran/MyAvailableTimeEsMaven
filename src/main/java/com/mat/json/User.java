package com.mat.json;

import java.util.List;

public class User {
    int userId;
    String email; //username
    String password;
    List<Scheduler> schedulers; //list of sheduler objects describing info about external services
    String timeZone;
    boolean format24; //flag containig true if 24 (18:00), false if 12 (6)
    String firstName;
    String lastName;
}

