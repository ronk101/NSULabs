package ru.nsu.skopintsev.commands;

import ru.nsu.skopintsev.Data;

public class IncrementPointer implements Commands{

    @Override
    public void execute(Data data) {
        data.incrementPtr();
    }
}
