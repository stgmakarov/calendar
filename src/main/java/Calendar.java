import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Calendar {
    final static String inputFileName = "input.txt";
    final static String outputName = "output.txt";

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new File(inputFileName));

        int n = in.nextInt();
        int m = in.nextInt();

        String boryanSheduler = in.nextLine();
        boryanSheduler = in.nextLine();
        boolean[] busyMins = getBusyMinutes(boryanSheduler);

        for (int l = 0; l < m; l++) {
            int x = in.nextInt();
            String nextSh = in.nextLine().replace(" ", "");
            try {
                busyMins = minusMinutes(busyMins, getBusyMinutes(nextSh));
            } catch (RuntimeException e) {
                saveFileData(outputName, "ALARM");
                return;
            }
        }
        String res = getMaxAvailableRange(busyMins);
        saveFileData(outputName, res);
        in.close();
    }

    public static boolean[] minusMinutes(boolean[] a, boolean[] b) {
        boolean[] result = a.clone();
        for (int i = 0; i < a.length; i++) {
            if (a[i] && b[i]) {
                result[i] = false;
            } else if (!a[i] && b[i]) {
                throw new RuntimeException();
            }
        }
        return result;
    }

    public static boolean[] getBusyMinutes(String schedule) {
        boolean[] busyMinutes = new boolean[24 * 60];
        String[] parts = schedule.split("-");
        String[] startParts = parts[0].split(":");
        String[] endParts = parts[1].split(":");
        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);
        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);
        for (int i = startHour * 60 + startMinute; i < endHour * 60 + endMinute; i++) {
            busyMinutes[i] = true;
        }
        return busyMinutes;
    }

    private static void saveFileData(String inputFileName, String result) {
        File file = new File(inputFileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMaxAvailableRange(boolean[] arr) {
        int maxStart = -1;
        int maxEnd = -1;
        int maxLen = 0;
        int curStart = -1;
        int curLen = 0;
        for(int i=0;i<arr.length;i++){
            if(arr[i]){
                if(curStart==-1) {
                    curStart = i;
                    curLen++;
                }else curLen++;
            } else {
                if(curStart!=-1) {
                    if(curLen>maxLen) {
                        maxLen = curLen;
                        maxStart  = curStart;
                        maxEnd = i;
                    }
                    curStart = -1;
                    curLen = 0;
                }
            }
        }
        if(maxStart==-1) return "ALARM";
        return String.format("%02d:%02d-%02d:%02d", maxStart / 60, maxStart % 60, maxEnd / 60, maxEnd % 60);
    }
}