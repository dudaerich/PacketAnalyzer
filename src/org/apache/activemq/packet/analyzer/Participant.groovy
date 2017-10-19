package org.apache.activemq.packet.analyzer

import org.apache.activemq.packet.analyzer.events.HandlePacketEvent
import org.apache.activemq.packet.analyzer.events.SendPacketEvent

class Participant {

    def sendPacketRegex = [
            /^(\d+:\d+:\d+,\d+)\s+TRACE\s+\[(.+)\]\s+\((.+)\)\s+Sending packet\s+([^)]+)\s+PACKET\(([^)]+)\)\[type=(\d+),\s+channelID=(\d+)/,
            /^(\d+:\d+:\d+,\d+)\s+TRACE\s+\[(.+)\]\s+\((.+)\)\s+Sending packet\s+([^)]+)\s+(\S+)\(/
    ]

    def handlePacketRegex = [
            /^(\d+:\d+:\d+,\d+)\s+TRACE\s+\[(.+)\]\s+\((.+)\)\s+handlePacket::handling PACKET\(([^)]+)\)/,
            /^(\d+:\d+:\d+,\d+)\s+TRACE\s+\[(.+)\]\s+\((.+)\)\s+handling packet\s+PACKET\(([^)]+)\)\[type=(\d+),\s+channelID=(\d+)/,
            /^(\d+:\d+:\d+,\d+)\s+TRACE\s+\[(.+)\]\s+\((.+)\)\s+handlePacket::handling\s+(\S+)\(.+\)/
    ]

    def sendPacketClientRegex = [
            /^(\d+:\d+:\d+,\d+)\s+(\S+)\s+TRACE\s+\[(.+)\]\s+Sending\s+(\S+)\s+PACKET\((.+)\)\[type=(\d+),\s+channelID=(\d+)/,
            /^(\d+:\d+:\d+,\d+)\s+(\S+)\s+TRACE\s+\[(.+)\]\s+Sending\s+packet\s+(\S+)\s+PACKET\((.+)\)\[type=(\d+),\s+channelID=(\d+)/
    ]

    def handlePacketClientRegex = [
            /^(\d+:\d+:\d+,\d+)\s+(.+)\s+TRACE\s+\[(.+)\]\s+handling packet\s+PACKET\((.+)\)\[type=(\d+),\s+channelID=(\d+)/
    ]

    String color

    def events = []

    Participant(String color) {
        this.color = color
    }

    void readLog(InputStream is) {

        is.eachLine { line ->


            for (def regex : sendPacketRegex) {
                def matcher = (line =~ regex)

                if (matcher) {
                    def event = new SendPacketEvent()
                    event.setTime(matcher[0][1])
                    event.setLogger(matcher[0][2])
                    event.setThread(matcher[0][3])
                    event.setBlocking(matcher[0][4])
                    event.setPacketType(matcher[0][5])
                    event.setPacketTypeNumber(matcher[0][6])
                    event.setChannelID(matcher[0][7])
                    event.setParticipant(this)
                    events.add(event)
                    return
                }
            }

            for (def regex : handlePacketRegex) {
                def matcher = (line =~ regex)

                if (matcher) {
                    def event = new HandlePacketEvent()
                    event.setTime(matcher[0][1])
                    event.setLogger(matcher[0][2])
                    event.setThread(matcher[0][3])
                    event.setPacketType(matcher[0][4])
                    event.setPacketTypeNumber(matcher[0][5])
                    event.setChannelID(matcher[0][6])
                    event.setParticipant(this)
                    events.add(event)
                    return
                }
            }

            for (def regex : sendPacketClientRegex) {
                def matcher = (line =~ regex)

                if (matcher) {
                    def event = new SendPacketEvent()
                    event.setTime(matcher[0][1])
                    event.setThread(matcher[0][2])
                    event.setLogger(matcher[0][3].replaceAll(/:\d+$/, ""))
                    event.setBlocking(matcher[0][4])
                    event.setPacketType(matcher[0][5])
                    event.setPacketTypeNumber(matcher[0][6])
                    event.setChannelID(matcher[0][7])
                    event.setParticipant(this)
                    events.add(event)
                    return
                }
            }

            for (def regex : handlePacketClientRegex) {
                def matcher = (line =~ regex)

                if (matcher) {
                    def event = new HandlePacketEvent()
                    event.setTime(matcher[0][1])
                    event.setThread(matcher[0][2])
                    event.setLogger(matcher[0][3].replaceAll(/:\d+$/, ""))
                    event.setPacketType(matcher[0][4])
                    event.setPacketTypeNumber(matcher[0][5])
                    event.setChannelID(matcher[0][6])
                    event.setParticipant(this)
                    events.add(event)
                    return
                }
            }
        }
    }

}
