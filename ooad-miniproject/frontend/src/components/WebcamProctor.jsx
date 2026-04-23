import { useEffect, useRef, useState } from 'react';
import * as faceapi from 'face-api.js';

const NO_FACE_THRESHOLD = 3; // 3 consecutive cycles
const MULTI_FACE_THRESHOLD = 3;
const INTERVAL_MS = 500; // 500 ms
const FACE_SIZE_MIN = 80; // px
const FACE_SEPARATION_MIN = 50; // px

export default function WebcamProctor({ onViolation, onMultipleFaces }) {
  const videoRef = useRef(null);
  const canvasRef = useRef(null);
  const intervalRef = useRef(null);
  const noFaceCount = useRef(0);
  const multiFaceCount = useRef(0);

  const [status, setStatus] = useState('Initializing...');

  useEffect(() => {
    console.log('🎥 Webcam component mounted');

    const startDetection = async () => {
      if (!videoRef.current) {
        console.warn('Video ref not ready');
        return;
      }

      if (intervalRef.current) {
        return; // Already running
      }

      console.log('🚦 Detection loop started');

      intervalRef.current = setInterval(async () => {
        if (!videoRef.current || videoRef.current.readyState < 2) {
          console.log('⏳ Video not ready for detection yet');
          return;
        }

        try {
          console.log('🔍 Running detection...');

          const detections = await faceapi
            .detectAllFaces(
              videoRef.current,
              new faceapi.TinyFaceDetectorOptions({ inputSize: 224, scoreThreshold: 0.5 })
            )
            .withFaceLandmarks();

          console.log('🧠 Detections:', detections.length);

          const validDetections = detections.filter(d => {
            const box = d.detection.box;
            return box.width >= FACE_SIZE_MIN && box.height >= FACE_SIZE_MIN;
          });

          const ctx = canvasRef.current.getContext('2d');
          canvasRef.current.width = videoRef.current.videoWidth;
          canvasRef.current.height = videoRef.current.videoHeight;
          ctx.clearRect(0, 0, canvasRef.current.width, canvasRef.current.height);

          faceapi.draw.drawDetections(canvasRef.current, validDetections);

          let hasSeparatedMultiFace = false;
          if (validDetections.length > 1) {
            hasSeparatedMultiFace = true;
          }

          if (validDetections.length === 0) {
            noFaceCount.current += 1;
            multiFaceCount.current = 0;
            if (noFaceCount.current >= NO_FACE_THRESHOLD) {
              setStatus('Suspicious Activity Detected');
              onViolation?.('FACE_NOT_DETECTED', 'HIGH', 'No face detected in camera feed');
            }
          } else if (hasSeparatedMultiFace) {
            multiFaceCount.current += 1;
            noFaceCount.current = 0;
            if (multiFaceCount.current >= MULTI_FACE_THRESHOLD) {
              setStatus('Suspicious Activity Detected');
              onMultipleFaces?.(true);
              onViolation?.('MULTIPLE_FACES', 'CRITICAL', 'Multiple faces detected in camera feed');
            }
          } else {
            noFaceCount.current = 0;
            multiFaceCount.current = 0;
            setStatus('Secure');
            onMultipleFaces?.(false);
          }
        } catch (detectionError) {
          console.error('Detection error:', detectionError);
          setStatus('Monitoring Active');
        }
      }, INTERVAL_MS);
    };

    const initWebcam = async () => {
      try {
        console.log('⏳ Loading models...');
        await faceapi.nets.tinyFaceDetector.loadFromUri('/models');
        await faceapi.nets.faceLandmark68Net.loadFromUri('/models');
        console.log('✅ Models loaded');

        const stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: false });
        videoRef.current.srcObject = stream;

        videoRef.current.onloadeddata = () => {
          console.log('✅ Video stream loaded');
          setStatus('Monitoring Active');
          startDetection();
        };

        videoRef.current.onended = () => {
          console.log('🔴 Video stream ended');
          setStatus('Monitoring Inactive');
        };
      } catch (error) {
        console.error('Webcam init error:', error);
        setStatus('Secure');
      }
    };

    initWebcam();

    return () => {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
      }
      const tracks = videoRef.current?.srcObject?.getTracks();
      tracks?.forEach(t => t.stop());
      setStatus('Monitoring Inactive');
      console.log('🛑 Webcam component unmounted');
    };
  }, [onViolation, onMultipleFaces]);

  return (
    <div className="bg-slate-50 border border-slate-200 rounded-lg p-4">
      <div className="relative w-[320px] h-[240px] rounded-md overflow-hidden">
        <video ref={videoRef} className="w-full h-full object-cover" muted autoPlay playsInline />
        <canvas
          ref={canvasRef}
          className="absolute inset-0 pointer-events-none"
          style={{ width: '100%', height: '100%' }}
        />
      </div>
      <div className="mt-2 text-center">
        <span
          className={`text-sm font-medium ${
            status === 'Secure' ? 'text-green-600' : status === 'Monitoring Active' ? 'text-blue-600' : 'text-amber-600'
          }`}>
          {status}
        </span>
      </div>
    </div>
  );
}

