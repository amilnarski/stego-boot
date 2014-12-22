import spock.lang.Specification
import stego.StegoTool

class StegoToolSpec extends Specification {
    def "getLSB(0)"() {
        when:
            def lsb = StegoTool.getLSB(0)
        then:
            lsb == 0
    }

    def "getLSB(1)"() {
        when:
            def lsb = StegoTool.getLSB(1)
        then:
            lsb == 1
    }

    def "getLSB(10)"() {
        when:
            def lsb = StegoTool.getLSB(10)
        then:
            lsb == 0
    }

    def "getLSB(11)"() {
        when:
            def lsb = StegoTool.getLSB(11)
        then:
            lsb == 1
    }

    def "getMSB(0)"() {
        when:
            def msb = StegoTool.getMSB(0)
        then:
            msb == 0
    }

    def "getMSB(1)"() {
        when:
            def msb = StegoTool.getMSB(1)
        then:
            msb == 0
    }

    def "getMSB of big number"() {
        setup:
            int x = 0b00000000000000001000000000000000
        when:
            def msb = StegoTool.getMSB(x)
        then:
            msb == 1
    }

    def "getMSB of big number w/ position 16"() {
        setup:
            int x = 0b00000000000000001000000000000000
        when:
            def msb = StegoTool.getBit(x, 16)
        then:
            msb == 1
    }

    def "getMSB of big number w/ position 8"() {
        setup:
            int x = 0b00000000000000000000000010000000
        when:
            def msb = StegoTool.getBit(x, 8)
        then:
            msb == 1
    }

    def "getMSB(MAX_VALUE)"() {
        setup:
            def x = Integer.MAX_VALUE
        when:
            def msb = StegoTool.getMSB(x)
        then:
            msb == 1
    }
}
