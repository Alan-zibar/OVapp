package Service;

    public class TripSearchState {

        private static String fromStation;
        private static String toStation;
        private static String selectedDate;
        private static String selectedTime;

        public static void saveSearch(String from, String to, String date, String time) {
            fromStation = from;
            toStation = to;
            selectedDate = date;
            selectedTime = time;
        }

        public static String getFromStation() {
            return fromStation;
        }

        public static String getToStation() {
            return toStation;
        }

        public static String getSelectedDate() {
            return selectedDate;
        }

        public static String getSelectedTime() {
            return selectedTime;
        }
    }


