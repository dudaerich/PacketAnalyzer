package org.apache.activemq.packet.analyzer

import org.apache.activemq.packet.analyzer.events.HandlePacketEvent
import org.apache.activemq.packet.analyzer.events.SendPacketEvent

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

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

    def events = Collections.synchronizedList([])

    Participant(String color) {
        this.color = color
    }

    void readLog(InputStream is) {

        int numberOfProcessors = Runtime.getRuntime().availableProcessors()
        int numberOfWorkers = numberOfProcessors - 1

        BlockingQueue<String> queue = new LinkedBlockingQueue<>(numberOfWorkers * 500)

        // prepare workers
        def workers = []

        for (int i = 0; i < numberOfWorkers; i++) {
            workers << new Worker(participant: this, queue: queue)
        }
        workers.forEach { it.start() }

        is.eachLine { queue.put(it) }

        workers.forEach { it.join() }
    }

    class Worker extends Thread {

        Participant participant
        BlockingQueue queue

        @Override
        void run() {
            def line
            while ((line = queue.poll(5, TimeUnit.SECONDS)) != null) {

                try {
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
                            event.setParticipant(participant)
                            events.add(event)
                            continue
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
                            event.setParticipant(participant)
                            events.add(event)
                            continue
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
                            event.setParticipant(participant)
                            events.add(event)
                            continue
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
                            event.setParticipant(participant)
                            events.add(event)
                            continue
                        }
                    }
                } catch (Exception e) {
                    System.err.println "Error during processing line $line - $e"
                    e.printStackTrace(System.err)
                }


            }
        }
    }

}
