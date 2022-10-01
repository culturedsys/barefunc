package systems.cultured.barefunc.catlist;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.jupiter.api.Test;

public class CatListTest {
    @Test
    public void sizeOfEmptyListIsZero() {
        assertThat(CatList.emptyCatList().size(), equalTo(0));
    }

    @Test
    public void nilEqualsEmptyList() {
        assertThat(CatList.emptyCatList(), equalTo(Collections.emptyList()));
    }

    @Test
    public void appendAddsElement() {
        assertThat(CatList.emptyCatList().append(1), equalTo(Collections.singletonList(1)));
    }

    @Test
    public void repeatedAppendAddsElements() {
        var list = CatList.<Integer>emptyCatList()
            .append(1)
            .append(2)
            .append(3);

        assertThat(list, equalTo(List.of(1, 2, 3)));
    }

    @Test
    public void concatAnotherCatListConcats() {
        var list = CatList.of(1, 2, 3).concat(CatList.of(4, 5, 6));

        assertThat(list, equalTo(List.of(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void concatAnotherListConcats() {
        var list = CatList.of(1, 2, 3).concat(List.of(4, 5, 6));

        assertThat(list, equalTo(List.of(1, 2, 3, 4, 5, 6)));
    }
}
