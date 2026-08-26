function setupPickupCamera() {
  var video = document.getElementById('cameraVideo');
  var placeholder = document.getElementById('cameraPlaceholder');
  var photo = document.getElementById('capturedPhoto');
  var canvas = document.getElementById('captureCanvas');
  var cameraBox = document.getElementById('cameraBox');
  var hiddenInput = document.getElementById('photoData');
  var statusText = document.getElementById('photoStatusText');
  var actionArea = document.getElementById('photoActionArea');
  var confirmBtn = document.getElementById('confirmBtn');
  var confirmModal = document.getElementById('confirmModal');
  var cancelConfirmBtn = document.getElementById('cancelConfirm');

  if (!video) return;

  var stream = null;

  function stopStream() {
    if (stream) {
      stream.getTracks().forEach(function (t) { t.stop(); });
      stream = null;
    }
  }

  function renderStartButton() {
    actionArea.innerHTML = '<button class="primary" type="button" id="startCameraBtn">開啟相機並拍攝</button>';
    document.getElementById('startCameraBtn').addEventListener('click', startCamera);
  }

  function renderCaptureButton() {
    actionArea.innerHTML = '<button class="primary" type="button" id="captureBtn">拍照</button>';
    document.getElementById('captureBtn').addEventListener('click', capturePhoto);
  }

  function renderRetakeButton() {
    actionArea.innerHTML = '<button class="secondary" type="button" id="retakeBtn">重新拍攝</button>';
    document.getElementById('retakeBtn').addEventListener('click', retake);
  }

  function startCamera() {
    navigator.mediaDevices.getUserMedia({ video: { facingMode: 'user' }, audio: false }).then(function (s) {
      stream = s;
      video.srcObject = s;
      placeholder.style.display = 'none';
      photo.style.display = 'none';
      video.style.display = 'block';
      cameraBox.classList.remove('captured');
      statusText.textContent = '相機預覽中，請對準來訪人臉部後拍照';
      renderCaptureButton();
    }).catch(function (err) {
      statusText.textContent = '無法開啟相機：' + (err && err.message ? err.message : '請確認已授權相機權限');
    });
  }

  function capturePhoto() {
    var w = video.videoWidth || 640;
    var h = video.videoHeight || 480;
    canvas.width = w;
    canvas.height = h;
    canvas.getContext('2d').drawImage(video, 0, 0, w, h);
    var dataUrl = canvas.toDataURL('image/jpeg', 0.85);
    hiddenInput.value = dataUrl;
    photo.src = dataUrl;

    stopStream();
    video.style.display = 'none';
    photo.style.display = 'block';
    cameraBox.classList.add('captured');
    statusText.textContent = '照片拍攝完成';
    renderRetakeButton();
    confirmBtn.disabled = false;
  }

  function retake() {
    hiddenInput.value = '';
    photo.style.display = 'none';
    confirmBtn.disabled = true;
    statusText.textContent = '尚未拍攝';
    startCamera();
  }

  renderStartButton();

  confirmBtn.addEventListener('click', function () {
    if (!hiddenInput.value) return;
    confirmModal.style.display = 'grid';
  });
  cancelConfirmBtn.addEventListener('click', function () {
    confirmModal.style.display = 'none';
  });
}

document.addEventListener('DOMContentLoaded', function () {
  if (document.getElementById('cameraVideo')) setupPickupCamera();
});
