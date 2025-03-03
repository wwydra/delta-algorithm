import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Main {
    static volatile boolean working = true;

    public static void main(String[] args) {

        Reader.readFile(Reader.testPath);
        Reader.readFile(Reader.trainingPath);

        Scanner scanner = new Scanner(System.in);
        String answer;

        Perceptron perceptron = new Perceptron(Iris.training_answers);
        perceptron.train();

        int correct = 0;
        int wrong = 0;

        for (Iris iris : Iris.toClassifyData)
            Perceptron.classify(iris);

        for(int i = 0; i < Iris.testData.size(); i++){
            if (Iris.testData.get(i).getType() == Iris.toClassifyData.get(i).getType())
                correct++;
            else
                wrong++;
        }

        double accuracy = (double) correct / (double) (correct + wrong) * 100;
        BigDecimal bd = new BigDecimal(Double.toString(accuracy));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        accuracy = bd.doubleValue();

        System.out.println("Accuracy of the test: " + accuracy + "%");

        do{
            do {
                System.out.println();
                System.out.println("Do you want to classify an iris? (yes/no)");
                answer = scanner.nextLine();
            }while (!(answer.equals("yes") || answer.equals("no")));

            switch (answer) {
                case "yes" -> {
                    OptionalInt maxValue = Iris.training_answers.stream().mapToInt(iris -> iris.getConditionalAttributes().size()).max();

                    List<Double> attributes = new ArrayList<>();
                    String[] parts;

                    for (int i = 0; i < Reader.numOfAttributes - 1; i++) {
                        System.out.println("Input value of attribute:");

                        answer = scanner.nextLine();
                        parts = answer.replace(',', '.').split(" ");

                        try {
                            for (String part : parts)
                                attributes.add(Double.parseDouble(part));
                        }catch (NumberFormatException e){
                            System.out.println("The value needs to be a number");
                            i--;
                        }
                    }

                    Iris newIris = new Iris(attributes, null);
                    Perceptron.classify(newIris);

                    System.out.println("Your iris was classified as " + newIris.getType());

                }
                case "no" -> working = false;
            }
        }while(working);
    }
}
