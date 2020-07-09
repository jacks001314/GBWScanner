package com.gbw.scanner.source.shodan;

import com.fooock.shodan.ShodanRestApi;
import com.fooock.shodan.model.host.HostReport;
import io.reactivex.observers.DisposableObserver;

import java.util.concurrent.atomic.AtomicInteger;

public class ShodanTest {

    public static void main(String[] args) {

        ShodanRestApi  api =new ShodanRestApi("WMc5E9NbsUk5vz4Y0TmeTdUQfqBr5eEx");

        int page = 0;


        AtomicInteger count = new AtomicInteger();
        final boolean[] isBreak = {false};
        while(!isBreak[0]) {
            api.hostSearch(page, "Flink", null)
                    .subscribe(
                            new DisposableObserver<HostReport>() {
                                @Override
                                public void onNext(HostReport hostReport) {

                                    System.out.println(hostReport.getTotal());
                                    hostReport.getBanners().forEach(e -> {
                                        System.out.println(e.getIpStr() + ":" + e.getPort());
                                        count.getAndIncrement();
                                    });

                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    throwable.printStackTrace();
                                    isBreak[0] = true;
                                }

                                @Override
                                public void onComplete() {

                                    //isBreak[0] = true;
                                }
                            }
                    );

            page++;
        }
        System.out.println(count.get());
    }

}
