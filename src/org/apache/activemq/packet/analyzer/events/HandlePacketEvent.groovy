package org.apache.activemq.packet.analyzer.events

import org.apache.activemq.packet.analyzer.Colors


/**
 * Created by eduda on 1.2.2017.
 */
class HandlePacketEvent extends Event {

    @Override
    String toString() {
        return Colors.getColor(participant.color) + time.format("HH:mm:ss,SSS") +" Handling " +
                "thread='" + thread + '\', ' +
                "packetType='" + packetType + '\', ' +
                "packetTypeNumber='" + packetTypeNumber + '\', ' +
                "channelID='" + channelID + '\''
    }

    String toHtml() {
        def fields = [
                time.format("HH:mm:ss,SSS"),
                'Handling',
                "packetType='$packetType'",
                "packetTypeNumber='$packetTypeNumber'",
                "channelID='$channelID'"
        ]
        return "<tr style=\"color: ${participant.color};\">" + fields.collect { "<td>$it</td>" }.join() + "</tr>"
    }
}
