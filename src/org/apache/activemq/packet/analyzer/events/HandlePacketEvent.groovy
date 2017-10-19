package org.apache.activemq.packet.analyzer.events

import org.apache.activemq.packet.analyzer.Colors


/**
 * Created by eduda on 1.2.2017.
 */
class HandlePacketEvent extends Event {

    String packetType

    String packetTypeNumber

    String channelID


    @Override
    String toString() {
        return Colors.getColor(participant.color) + time.format("HH:mm:ss,SSS") +" Handling " +
                "thread='" + thread + '\', ' +
                "packetType='" + packetType + '\', ' +
                "packetTypeNumber='" + packetTypeNumber + '\', ' +
                "channelID='" + channelID + '\''
    }
}
