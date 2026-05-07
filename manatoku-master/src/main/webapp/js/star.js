/**
 * 1. 테마 전환 및 상태 유지
 */
function toggleTheme() {
    // chat.jsp의 최상위 컨테이너 클래스인 chat-wrapper를 타겟으로 합니다.
    const chatWin = document.querySelector('.chat-wrapper');
   const body = document.body;

// 패널 색상을 바꾸는 핵심은 바로 이 body 클래스입니다!
    body.classList.toggle('galaxy-theme');

    // 2. 클래스 토글 (채팅창 전용 클래스와 전체 바디용 클래스 모두 토글)
    if (!chatWin) return; // 요소가 없으면 실행 중단
    chatWin.classList.toggle('theme-galaxy');
   
  // 3. 현재 상태 확인
    const isGalaxy = chatWin.classList.contains('theme-galaxy');

    // 4. 상태 저장 및 UI(아이콘/텍스트) 업데이트
    localStorage.setItem('selected-theme', isGalaxy ? 'galaxy' : 'default');
    updateThemeUI(isGalaxy);
}

function updateThemeUI(isGalaxy) {
    const icon = document.getElementById('theme-icon');
    const text = document.getElementById('theme-text');
    if (icon && text) {
        icon.innerText = isGalaxy ? "☀️" : "✨";
    text.innerText = isGalaxy ? "デフォルトテーマ" : "天の川テーマ";
    }
}



/**
 * 2. 초기 로드 및 이벤트 연결
 */
document.addEventListener('DOMContentLoaded', () => {
    const savedTheme = localStorage.getItem('selected-theme');
    const isGalaxy = (savedTheme === 'galaxy');

    // 초기 상태 복원
    if (isGalaxy) {
        document.body.classList.add('galaxy-theme');
        const chatWin = document.querySelector('.chat-wrapper');
        if (chatWin) chatWin.classList.add('theme-galaxy');
    }
    updateThemeUI(isGalaxy);

    // 이벤트 바인딩
    const toggleBtn = document.getElementById('theme-icon');
    if (toggleBtn) toggleBtn.onclick = toggleTheme;
});


/**
 * 2. 메시지 수신 시 효과
 */
function onReceiveMessage(data) {
    const chatWin = document.querySelector('.chat-wrapper');
    if (!chatWin) return;

    const isGalaxyMode = chatWin.classList.contains('theme-galaxy');

    // 메시지 렌더링 (기존 채팅창 ID인 chatMessageArea 사용)
    // displayMessage(data); 

    if (isGalaxyMode && data.type === "TALK") {
        const starLayer = document.querySelector('.stars');
        if (starLayer) {
            starLayer.style.animation = 'none';
            void starLayer.offsetWidth; // 리플로우 강제 발생 (애니메이션 재시작용)
            starLayer.style.animation = 'stars-move 100s infinite linear, sparkle-flash 0.5s';
        }
        createShootingStar();
    }
}

function createShootingStar() {
const wrapper = document.getElementById('chatWrapper');
    // theme-galaxy 클래스가 있을 때만 실행!
    if (!wrapper || !wrapper.classList.contains('theme-galaxy')) return;

    const container = document.querySelector('.shooting-stars');
    if (!container) return;

    const star = document.createElement('div');
    star.className = 'shooting-star';
   
    // 위치 랜덤 설정
    star.style.left = (Math.random() * 80 + 20) + '%'; // 오른쪽 상단 위주 시작
    star.style.top = (Math.random() * 50) + '%';
    // 기존 CSS에 fixed된 위치가 있다면 무효화
    star.style.right = 'auto';
   
    container.appendChild(star);

    // CSS 애니메이션 시간(3s)에 맞춰서 삭제
    setTimeout(() => {
        if(star) star.remove();
    }, 3000);
}

/**
 * 3. 초기 로드 시 설정
 */
window.addEventListener('DOMContentLoaded', () => {
    const savedTheme = localStorage.getItem('selected-theme');
    const chatWin = document.querySelector('.chat-wrapper');
    
    // 페이지가 처음 로드될 때(또는 숨겨져 있다가 보일 때) 상태 복구
    if (savedTheme === 'galaxy' && chatWin) {
        chatWin.classList.add('theme-galaxy');
        updateThemeUI(true);
    }
});