package com.example.smartbits.vehicleservicingapp.loginandregistration;

/**
 * Created by root on 30/3/17.
 */

public class Config {
    private static String url = "http://172.26.51.17/vehicleService";

    // User Login URL
    public static String URL_LOGIN = url+"/login.php";

    // User Signup URL

    public static String URL_SIGNUP = url+"/Register.php";

    // Car Signup URL
    public static String  URL_REG_CAR = url+"/registercars.php";

    // Service Booking URL
    public static String URL_BOOK_SERVICE = url+"/book.php";

    // Service Centers URL
    public static String URL_SERVICE_CENTERS = url+"/servicecenters.php";

    // Register a new service center
    public static String URL_REGISTER_SERVICE_CENTERS = url+"/registerservicecenters.php";

    // Fetch cars url
    public static String URL_FETCH_CARS = url+"/fetchCars.php";

    // Fetch all appointments
    public static String URL_FETCH_APPOINTMENTS = url+"/appointments.php";

    // send confirmation email
    public static String URL_SEND_CONFIRMATION = url+"/sendmail.php";
}
