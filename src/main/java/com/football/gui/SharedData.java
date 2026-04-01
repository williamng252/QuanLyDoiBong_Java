package com.football.gui;

import java.util.ArrayList;
import java.util.List;

public class SharedData {
    public static List<String> teams = new ArrayList<>();
    public static List<Runnable> teamListeners = new ArrayList<>();
    
    public static int totalTeams = 2; 
    public static int totalPlayers = 3; 
    public static double userBudget = 100_000_000;

    public static List<String> activities = new ArrayList<>();

    public static List<Runnable> dashboardListeners = new ArrayList<>();

    static {
        logActivity("Hệ thống khởi động thành công.");
    }

    public static void logActivity(String msg) {
        String time = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
        activities.add(0, time + " - " + msg);
        notifyDashboardChanged();
    }

    public static void notifyTeamsChanged() {
        for (Runnable r : teamListeners) r.run();
        notifyDashboardChanged();
    }

    public static void notifyDashboardChanged() {
        for (Runnable r : dashboardListeners) r.run();
    }
}
