package machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CoffeeMachine {

    private int waterInMachine = 400;
    private int milkInMachine = 540;
    private int coffeeBeansInMachine = 120;
    private int disposableCupsInMachine = 9;
    private int moneyInMachine = 550;

    private CoffeeMachineState machineState = CoffeeMachineState.SHOW_ACTION_MENU;

    public static void main(String[] args) {

        CoffeeMachine coffeeMachine1 = new CoffeeMachine();

        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        while ( ! coffeeMachine1.machineStateIsExit()) {

            coffeeMachine1.processUserInput(userInput);

            if ( coffeeMachine1.needUserInputStage() && ! coffeeMachine1.machineStateIsExit()) {
                userInput = scanner.next();
            }
        }
    }

    private boolean machineStateIsExit() {
        return machineState.equals(CoffeeMachineState.EXIT);
    }

    private void processUserInput(String userInput) {
        switch (machineState) {
            case SHOW_ACTION_MENU:
                showChooseActionMessage();
                machineState = CoffeeMachineState.CHOOSING_AN_ACTION;
                break;
            case CHOOSING_AN_ACTION:
                processActionMenu(userInput);
                break;
            case SHOW_BUY_ACTION_MENU:
                showChooseBuyMenuMessage();
                machineState = CoffeeMachineState.PROCESS_BUY_ACTION;
                break;
            case PROCESS_BUY_ACTION:
                processBuyAction(userInput);
                machineState = CoffeeMachineState.SHOW_ACTION_MENU;
                break;
            case PROCESS_FILL_ACTION:
                machineState = CoffeeMachineState.SHOW_FILL_WATER_MESSAGE;
                break;
            case SHOW_FILL_WATER_MESSAGE:
                showFillMessage("water", "ml");
                machineState = CoffeeMachineState.PROCESS_FILL_WATER;
                break;
            case PROCESS_FILL_WATER:
                processFillWater(userInput);
                machineState = CoffeeMachineState.SHOW_FILL_MILK_MESSAGE;
                break;
            case SHOW_FILL_MILK_MESSAGE:
                showFillMessage("milk", "ml");
                machineState = CoffeeMachineState.PROCESS_FILL_MILK;
                break;
            case PROCESS_FILL_MILK:
                processFillMilk(userInput);
                machineState = CoffeeMachineState.SHOW_FILL_COFFEE_BEANS_MESSAGE;
                break;
            case SHOW_FILL_COFFEE_BEANS_MESSAGE:
                showFillMessage("coffee beans", "grams");
                machineState = CoffeeMachineState.PROCESS_FILL_COFFEE_BEANS;
                break;
            case PROCESS_FILL_COFFEE_BEANS:
                processFillCoffeeBeans(userInput);
                machineState = CoffeeMachineState.SHOW_FILL_DISPOSABLE_CUPS_MESSAGE;
                break;
            case SHOW_FILL_DISPOSABLE_CUPS_MESSAGE:
                showFillMessage("coffee", "disposable cups");
                machineState = CoffeeMachineState.PROCESS_FILL_DISPOSABLES_CUPS;
                break;
            case PROCESS_FILL_DISPOSABLES_CUPS:
                processFillDisposableCups(userInput);
                machineState = CoffeeMachineState.SHOW_ACTION_MENU;
                break;
            case EXIT:
                break;
            default:
                System.out.println("Invalid action.");
                break;
        }
    }

    private void showChooseActionMessage() {
        System.out.println("Write action (buy, fill, take, remaining, exit):");
    }

    private void showChooseBuyMenuMessage() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
    }

    private void processActionMenu(String action) {

        switch (action) {
                case "buy":
                    machineState = CoffeeMachineState.SHOW_BUY_ACTION_MENU;
                    break;
                case "fill":
                    machineState = CoffeeMachineState.PROCESS_FILL_ACTION;
                    break;
                case "take":
                    processTakeAction();
                    machineState = CoffeeMachineState.SHOW_ACTION_MENU;
                    break;
                case "remaining":
                    displayNumberOfIngredients();
                    machineState = CoffeeMachineState.SHOW_ACTION_MENU;
                    break;
                case "exit":
                    machineState = CoffeeMachineState.EXIT;
                    break;
                default:
                    System.out.println("Invalid action.");
                    break;
            }

    }

    private void processBuyAction(String buyOptionOrBack) {
        if ( ! buyOptionOrBack.equals("back")) {

            int typeOfCoffee = Integer.valueOf(buyOptionOrBack);

            switch (typeOfCoffee) {
                case 1:
                    prepareEspresso();
                    break;
                case 2:
                    prepareLatte();
                    break;
                case 3:
                    prepareCappuccino();
                    break;
                default:
                    System.out.println("Incorrect value for type of coffee");
                    break;
            }
        }
    }

    private void prepareEspresso() {
        prepareCoffee(250, 0, 16, 4);
    }

    private void prepareLatte() {
        prepareCoffee(350, 75, 20, 7);
    }

    private void prepareCappuccino() {
        prepareCoffee(200, 100, 12, 6);
    }

    private void prepareCoffee(int waterNeeded, int milkNeeded, int coffeeBeansNeeded, int price) {

        if (haveEnoughResourcesToMakeCoffee(waterNeeded, milkNeeded, coffeeBeansNeeded)) {
            System.out.println("I have enough resources, making you a coffee!");

            waterInMachine -= waterNeeded;
            milkInMachine -= milkNeeded;
            coffeeBeansInMachine -= coffeeBeansNeeded;
            moneyInMachine += price;

            disposableCupsInMachine--; // Any type of coffee requires 1 disposable cup
        }
    }

    private boolean haveEnoughResourcesToMakeCoffee(int waterNeeded, int milkNeeded, int coffeeBeansNeeded) {
        boolean enoughResources = true;

        if (waterNeeded > waterInMachine || milkNeeded > milkInMachine || coffeeBeansNeeded > coffeeBeansInMachine) {
            enoughResources = false;
            String missingResource = "";
            if (waterNeeded > waterInMachine) {
                missingResource = "water";
            } else if (milkNeeded > milkInMachine) {
                missingResource = "milk";
            } else if (coffeeBeansNeeded > coffeeBeansInMachine) {
                missingResource = "coffee beans";
            } else if (1 > disposableCupsInMachine) {
                missingResource = "disposable cups";
            }
            System.out.println("Sorry, not enough " + missingResource + "!");
        }

        return enoughResources;
    }

    private void showFillMessage(String resource, String unit) {
        System.out.println("Write how many "+ unit +" of " + resource + " do you want to add:");
    }

    private void processFillWater(String userInput) {
        int waterToAdd = Integer.valueOf(userInput);
        waterInMachine += waterToAdd;
    }

    private void processFillMilk(String userInput) {
        int milkToAdd = Integer.valueOf(userInput);
        milkInMachine += milkToAdd;
    }

    private void processFillCoffeeBeans(String userInput) {
        int coffeeBeansToAdd = Integer.valueOf(userInput);
        coffeeBeansInMachine += coffeeBeansToAdd;
    }

    private void processFillDisposableCups(String userInput) {
        int disposableCups = Integer.valueOf(userInput);
        disposableCupsInMachine += disposableCups;
    }

    private void processTakeAction() {
        System.out.println("I gave you $" + moneyInMachine);
        moneyInMachine = 0;
    }

    private void displayNumberOfIngredients() {
        System.out.println("The coffee machine has:");
        System.out.println(waterInMachine + " of water");
        System.out.println(milkInMachine + " of milk");
        System.out.println(coffeeBeansInMachine + " of coffee beans");
        System.out.println(disposableCupsInMachine + " of disposable cups");
        System.out.println(moneyInMachine + " of money");
    }

    private boolean needUserInputStage() {
        List<CoffeeMachineState> statesThatNeedUserInput = new ArrayList<CoffeeMachineState>(
                Arrays.asList(CoffeeMachineState.CHOOSING_AN_ACTION,
                              CoffeeMachineState.PROCESS_BUY_ACTION,
                              CoffeeMachineState.PROCESS_FILL_WATER,
                              CoffeeMachineState.PROCESS_FILL_MILK,
                              CoffeeMachineState.PROCESS_FILL_COFFEE_BEANS,
                              CoffeeMachineState.PROCESS_FILL_DISPOSABLES_CUPS));

        return statesThatNeedUserInput.contains(machineState);
    }

}

enum CoffeeMachineState {
    SHOW_ACTION_MENU,
    CHOOSING_AN_ACTION,

    SHOW_BUY_ACTION_MENU,
    PROCESS_BUY_ACTION,

    PROCESS_FILL_ACTION,

    SHOW_FILL_WATER_MESSAGE,
    SHOW_FILL_MILK_MESSAGE,
    SHOW_FILL_COFFEE_BEANS_MESSAGE,
    SHOW_FILL_DISPOSABLE_CUPS_MESSAGE,

    PROCESS_FILL_WATER,
    PROCESS_FILL_MILK,
    PROCESS_FILL_COFFEE_BEANS,
    PROCESS_FILL_DISPOSABLES_CUPS,


    EXIT {
        @Override
        public String toString() {
            return "exit";
        }
    };
}

enum CoffeeOptions {
    ESPRESSO {
        @Override
        public String toString() {
            return "espresso";
        }
    },
    LATTE {
        @Override
        public String toString() {
            return "latte";
        }
    },
    CAPPUCCINO {
        @Override
        public String toString() {
            return "cappuccino";
        }
    }
}
