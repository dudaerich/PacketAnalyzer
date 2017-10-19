import org.apache.activemq.packet.analyzer.Participant
import org.apache.activemq.packet.analyzer.events.Event

def participants = []

for (String arg : args) {
    def s = arg.split("=")
    Participant participant = new Participant(s[0])
    InputStream is = new FileInputStream(s[1])
    participant.readLog(is)
    is.close()
    participants.add(participant)
}

def events = []

for (Participant participant : participants) {
    events.addAll(participant.events)
}

//def filters = [
//        { it.packetType == 'ReplicationStartSyncMessage' && it.logger != 'org.apache.activemq.artemis.core.protocol.core.impl.RemotingConnectionImpl' }, // this packet is logged twice
//        { it.packetType == 'ReplicationResponseMessageV2' },
//        { it.packetType == 'ReplicationResponseMessage' },
//        { it.packetType == 'ReplicationSyncFileMessage' },
//        { it.packetType == 'ReplicationLargeMessageWriteMessage' },
//]

//def filters = [
//        { it.packetType == 'SessionReceiveMessage' && it.channelID == '11' },
//        { it.packetType == 'SessionSendMessage' && it.channelID == '11' },
//        { it.packetType == 'SessionAcknowledgeMessage' && it.channelID == '11' },
//        { it.packetType == 'NullResponseMessage' && it.channelID == '11' },
//        { it.packetTypeNumber == '43' && it.channelID == '11' },
//        { it.packetType == 'SessionConsumerFlowCreditMessage' && it.channelID == '11' },
//        { it.packetType == 'SessionAddMetaDataMessageV2' && it.channelID == '11' },
//        { it.packetType == 'SessionBindingQueryMessage' && it.channelID == '11' },
//        { it.packetType == 'SessionBindingQueryResponseMessage_V3' && it.channelID == '11' },
//]

def filters = [
        { it.channelID == '11' }
]

events = events.findAll {
    for (f in filters) {
        if (f(it)) return true
    }
    return false
}

events.sort { it.time }

for (Event e : events) {
    println e
}

println "${(char)27}[0m"