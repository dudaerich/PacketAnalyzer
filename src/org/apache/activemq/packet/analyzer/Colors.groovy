package org.apache.activemq.packet.analyzer

class Colors {

    static final colors = [
            default: 0,
            black: 30,
            red: 31,
            green: 32,
            yellow: 33,
            blue: 34,
            magenta: 35,
            cyan: 36,
            white: 37
    ]

    static String getColor(String name) {
        return "${(char)27}[${colors[name.toLowerCase()]}m"
    }

}
