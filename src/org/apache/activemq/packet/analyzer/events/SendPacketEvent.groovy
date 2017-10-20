package org.apache.activemq.packet.analyzer.events

import org.apache.activemq.packet.analyzer.Colors

class SendPacketEvent extends Event {

    String blocking

    @Override
    String toString() {
        return Colors.getColor(participant.color) + time.format("HH:mm:ss,SSS") + " Sending  " +
                "thread='" + thread + '\', ' +
                "packetType='" + packetType + '\', ' +
                "packetTypeNumber='" + packetTypeNumber + '\', ' +
                "blocking='" + blocking + '\', ' +
                "channelID='" + channelID + '\''
    }

    String toHtml() {
        def fields = [
                time.format("HH:mm:ss,SSS"),
                'Sending',
                "packetType='$packetType'",
                "packetTypeNumber='$packetTypeNumber'",
                "channelID='$channelID'"
        ]
        return "<tr style=\"color: ${participant.color};\">" + fields.collect { "<td>$it</td>" }.join() + "</tr>"
    }

}
