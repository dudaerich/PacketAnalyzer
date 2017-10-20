package org.apache.activemq.packet.analyzer.events

import org.apache.activemq.packet.analyzer.Participant

import java.text.SimpleDateFormat

class Event {

    final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss,SSS")

    Date time

    String logger

    String thread

    String channelID

    String packetType

    String packetTypeNumber

    Participant participant

    void setTime(String t) {
        time = formatter.parse(t)
    }

    Integer getChannelIDInt() {
        channelID?.toInteger()
    }

}
