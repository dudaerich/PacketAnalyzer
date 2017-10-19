package org.apache.activemq.packet.analyzer.events

import org.apache.activemq.packet.analyzer.Colors

class SendPacketEvent extends Event {

    String packetType

    String packetTypeNumber

    String blocking

    String channelID

    @Override
    String toString() {
        return Colors.getColor(participant.color) + time.format("HH:mm:ss,SSS") + " Sending  " +
                "thread='" + thread + '\', ' +
                "packetType='" + packetType + '\', ' +
                "packetTypeNumber='" + packetTypeNumber + '\', ' +
                "blocking='" + blocking + '\', ' +
                "channelID='" + channelID + '\''
    }

}
