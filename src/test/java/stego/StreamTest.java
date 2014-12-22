package stego;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class StreamTest {
    @Test
    public void orderIsPreserved(){
        int[][] arrays = new int[1000][1000];
        List<Integer> ints = new ArrayList<>();
        for(int i = 0; i<arrays.length; i++){
            for (int j = 0; j<arrays.length; j++){
                int k = i * arrays.length + j;
                arrays[i][j] = k;
                ints.add(k);
            }
        }

        final List<Integer> collect = Arrays.stream(arrays).parallel().flatMapToInt(Arrays::stream).parallel().boxed().collect(toList());
        assert ints.equals(collect);
    }
}
