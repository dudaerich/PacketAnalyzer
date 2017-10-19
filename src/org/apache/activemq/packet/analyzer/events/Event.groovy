package org.apache.activemq.packet.analyzer.events

import org.apache.activemq.packet.analyzer.Participant

import java.text.SimpleDateFormat

class Event {

    final static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss,SSS")

    Date time

    String logger

    String thread

    Participant participant

    void setTime(String t) {
        time = formatter.parse(t)
    }

}
