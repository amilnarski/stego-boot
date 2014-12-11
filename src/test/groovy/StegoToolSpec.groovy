import spock.lang.Specification
import stego.StegoTool

class StegoToolSpec extends Specification{
    def "getLSB(0)"(){
        when:
        def lsb = StegoTool.getLSB(0)
        then:
        lsb == 0
    }

    def "getLSB(1)"(){
        when:
        def lsb = StegoTool.getLSB(1)
        then:
        lsb == 1
    }

    def "getLSB(10)"(){
        when:
        def lsb = StegoTool.getLSB(10)
        then:
        lsb == 0
    }

    def "getLSB(11)"(){
        when:
        def lsb = StegoTool.getLSB(11)
        then:
        lsb == 1
    }

    def "getMSB(0)"(){
        when:
        def msb = StegoTool.getMSB(0)
        then:
        msb == 0
    }

    def "getMSB(1)"(){
        when:
        def msb = StegoTool.getMSB(1)
        then:
        msb == 0
    }

    def "getMSB(MAX_VALUE/2+1)"(){
        setup:
        def x = (Integer.MAX_VALUE/2)+1 as int
        when:
        def msb = StegoTool.getMSB(x)
        then:
        msb == 1
    }

    def "getMSB(MAX_VALUE)"(){
        setup:
        def x = Integer.MAX_VALUE
        when:
        def msb = StegoTool.getMSB(x)
        then:
        msb == 1
    }
}
