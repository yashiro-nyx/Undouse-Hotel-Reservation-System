package undouse_hotel.view;

import javax.swing.*;

public interface IPanelView {

    JPanel createPanel();

    void updatePanel();

    String getPanelName();
}