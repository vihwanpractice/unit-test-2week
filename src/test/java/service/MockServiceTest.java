package service;

import domain.Champion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.MockRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockServiceTest {

    @Mock
    private MockRepository mockRepository;

    @InjectMocks
    private MockService mockService;

    // ******************************************
    // 기본 mock test method 연습
    // ******************************************

    @Test
    public void 챔피언이름을가져오면_무조건_카이사를_리턴한다() {
        Champion champion = mock(Champion.class);
        champion.setPosition("정글");
        //champion.getName()을 호출하면 "카이사"를 리턴한다.
//        when(champion.getName()).thenReturn("카이사");
//        assertThat(champion.getName(), is("카이사"));
        when(champion.getPosition()).thenReturn("바텀");
//        assertThat(champion.getPosition(), is("정글"));

    }

    // 1. when, thenReturn을 사용하여 어떠한 챔피언 이름을 입력해도 베인을 리턴하도록 테스트하세요
    @Test
    public void shouldReturnVainWhenGetChampionName() {
        Champion champion = mock(Champion.class);
        when(champion.getName()).thenReturn("베인");
        assertThat(champion.getName(), is("베인"));
    }


    // 2. 챔피언 이름으로 야스오를 저장하면, doThrow를 사용하여 Exception이 발생하도록 테스트 하세요.
    @Test
    public void 야스오는과학이니익셉션이발생한다() {
        Champion champion = mock(Champion.class);
        doThrow(new IllegalArgumentException()).when(champion).setName("야스오");
    }


    // 3. verify 를 사용하여 '미드' 포지션을 저장하는 프로세스가 진행되었는지 테스트 하세요.
    @Test
    public void 미드포지션저장테스트() {
        Champion champion = mock(Champion.class);
        champion.setPosition("미드");
        champion.setPosition("미드");
        verify(champion).setPosition(anyString());
        verify(champion, times(2)).setPosition(anyString());
    }


    // 4. champion 객체의 크기를 검증하는 로직이 1번 실행되었는지 테스트 하세요.
    @Test
    public void 객체크기검증테스트() {
        Champion champion = mock(Champion.class);
        List<Champion> champions = mock(List.class);
        System.out.println(champions.size()); //verify보다 먼저 앞에서 한번 실행되어야함

        verify(champions, atLeastOnce()).size();
    }


    // 4-1. champion 객체에서 이름을 가져오는 로직이 2번 이상 실행되면 Pass 하는 로직을 작성하세요.
    @Test
    public void 객체이름2번이상가져오기() {
        Champion champion = mock(Champion.class);
        List<Champion> champions = mock(List.class);
        System.out.println(champions.size());
        System.out.println(champions.size());
        verify(champions, atLeast(2)).size();
    }


    // 4-2. champion 객체에서 이름을 가져오는 로직이 최소 3번 이하 실행되면 Pass 하는 로직을 작성하세요.
    @Test
    public void 객체이름최대3번이하실행() {
        List<Champion> champions = mock(List.class);
        Champion champion = mock(Champion.class);
        System.out.println(champions.size());
        System.out.println(champions.size());
        System.out.println(champions.size());

        champion.getPosition();
        champion.getPosition();
        champion.getPosition();
        champion.getPosition();
        verify(champions, atMost(3)).size();
        verify(champion, atMost(3)).getPosition();
    }

    // 4-3. champion 객체에서 이름을 저장하는 로직이 실행되지 않았으면 Pass 하는 로직을 작성하세요.
    @Test
    public void 객체이름저장실행안되면패스() {
        Champion champion = mock(Champion.class);
//        champion.getName();
        verify(champion, never()).getName();
        verify(champion, never()).setName(any(String.class));
    }


    // 4-4. champion 객체에서 이름을 가져오는 로직이 200ms 시간 이내에 1번 실행되었는 지 검증하는 로직을 작성하세요.
    @Test
    public void 객체이름을200ms이내에1번실행() throws InterruptedException {
        Champion champion = mock(Champion.class);
        Thread.sleep(1000);
        champion.getName();
        verify(champion, timeout(200).atLeastOnce()).getName();

    }


    // ******************************************
    // injectmock test 연습
    // ******************************************

    @Test
    public void 챔피언정보들을Mocking하고Service메소드호출테스트() {
/*        when(mockService.findByName(anyString())).thenReturn(new Champion("루시안", "바텀", 5));
        String championName = mockService.findByName("애쉬").getName();
        assertThat(championName, is("루시안"));
        verify(mockRepository, times(1)).findByName(anyString());*/

        when(mockService.findByName("조이")).thenReturn(new Champion("조이","미드",1));
        String champName = mockService.findByName("조이").getName();
        assertThat(champName, is("조이")); //실제 값을 체크
        verify(mockRepository, times(1)).findByName(anyString()); //값을 체크할때 findByName 로직이 제대로 실행되는지

    }

    // 1. 리산드라라는 챔피언 이름으로 검색하면 미드라는 포지션과 함께 가짜 객체를 리턴받고, 포지션이 미드가 맞는지를 테스트하세요
    @Test
    public void 리산드라검색하면미드로리턴받고맞는지테스트(){
        when(mockService.findByName("리산드라")).thenReturn(new Champion("리산드라","미드",3));
        String champPosition = mockService.findByName("리산드라").getPosition();
        assertThat(champPosition, is("미드"));
        verify(mockRepository, times(1)).findByName(anyString());
    }

    // 2. 2개 이상의 챔피언을 List로 만들어 전체 챔피언을 가져오는 메소드 호출시 그 갯수가 맞는지 확인하는 테스트 코드를 작성하세요.
    @Test
    public void 챔피언2개이상List만들고전체챔피언메소드호출시갯수확인테스트() {
        List<Champion> champions = mock(List.class);
        champions.add(new Champion("new","mid", 1));
        champions.add(new Champion("new2","jungle", 0));
        System.out.println(champions.size());
//        assertThat(champions.size(), is(2));
//        when(mockService.findAllChampions()).thenReturn();

        //verify(champions.size());
    }


    // 3. 챔피언을 검색하면 가짜 챔피언 객체를 리턴하고, mockRepository의 해당 메소드가 1번 호출되었는지를 검증하고, 그 객체의 스킨 개수가
    //    맞는지 확인하는 테스트코드를 작성하세요.
    @Test
    public void 검색시가짜챔피언객체리턴후메소드1번호출검증스킨갯수확인(){
        when(mockService.findByName(anyString())).thenReturn(new Champion("바이","정글",3));
        int champSkinCount = mockService.findByName("바이").getHasSkinCount();
        assertThat(champSkinCount, is(3));
        verify(mockRepository, times(1)).findByName(anyString());
    }


    // 4. 2개 이상의 가짜 챔피언 객체를 List로 만들어 리턴하고, 하나씩 해당 객체를 검색한 뒤 검색을 위해 호출한 횟수를 검증하세요.




    //가장 많이 사용되는 테스트 중 하나로 BDD 방식에 기반한 테스트 방법 예제
    //BDD - Behavior Driven Development
    @Test
    public void 탐켄치를_호출하면_탐켄치정보를_리턴하고_1번이하로_호출되었는지_검증() {
        //given
        given(mockRepository.findByName("탐켄치")).willReturn(new Champion("탐켄치", "서폿", 4));
        //when
        Champion champion = mockService.findByName("탐켄치");
        //then
        verify(mockRepository, atLeast(1)).findByName(anyString());
        assertThat(champion.getName(), is("탐켄치"));
    }

    @Test
    public void 탐켄치를_호출하면_탐켄치정보를_리턴하고_2번_호출되었는지_검증() {
        when(mockRepository.findByName("탐켄치")).thenReturn(new  Champion("탐켄치", "서폿", 4));
        Champion champion = mockService.findByName("탐켄치");
        System.out.println(mockRepository.findByName("탐켄치"));
        //then
        verify(mockRepository, times(2)).findByName(anyString());
        assertThat(champion.getName(), is("탐켄치"));
    }
}