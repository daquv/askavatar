package com.daquv.Service;

import com.daquv.Utils.NaverStt;
import com.daquv.Utils.NaverTts;
import com.daquv.Utils.microPhoneRecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daquv.Repository.IndexRepository;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;


@Service
public class IndexService {

	@Autowired
    private IndexRepository indexrepo;

	@Autowired
	private NaverStt naverstt;

	@Autowired
	private NaverTts navertts;


	public String getTest() {
		
		return indexrepo.getTest();
	}

    public String convertToString() throws Exception {

		// 음성파일 만들기
//		microPhoneRecoder mr = new microPhoneRecoder(new AudioFormat(16000,16,1, true, false));
//		mr.start();
//		Thread.sleep(5 * 1000);
//		mr.stop();
//		Thread.sleep(1000);
//
//        //save
//
//		File file = new File("test.wav");
//		try {
//			AudioSystem.write(mr.getAudioInputStream(), AudioFileFormat.Type.WAVE,file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		// stt 발화로 저장된 파일경로를 넘긴다.
		String sttText = naverstt.naverStt("asdfasdf");

		// tts : stt에서 나온 결과를 넘긴다.
		String ttsText = navertts.naverTTS(sttText);

		return ttsText;
    }
}
