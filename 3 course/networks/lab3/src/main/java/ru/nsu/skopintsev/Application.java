package ru.nsu.skopintsev;

import ru.nsu.skopintsev.controller.MainController;

public class Application {
    public static void main(String[] args) {
        MainController mainController = new MainController();
        mainController.start();
    }
}
