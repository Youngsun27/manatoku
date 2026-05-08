/**
 * 1. 테마 전환 및 상태 유지
 */
function toggleTheme() {
    const body = document.body;
    const mainWrapper = document.querySelector('.main-wrapper');

    // body 클래스만으로 테마 관리
    body.classList.toggle('galaxy-theme');

    /* 메인 페이지에도 테마 적용
    if (mainWrapper) {
        mainWrapper.classList.toggle('theme-galaxy');
    } */

    // 현재 상태 확인
    const isGalaxy = body.classList.contains('galaxy-theme');

    // 상태 저장 및 UI 업데이트
    localStorage.setItem('selected-theme', isGalaxy ? 'galaxy' : 'default');
    updateThemeUI(isGalaxy);
}


/**
 * 2. 테마 UI 업데이트 (아이콘 및 텍스트)
 */
function updateThemeUI(isGalaxy) {
    const icon = document.getElementById('theme-icon');
    const text = document.getElementById('theme-text');

    if (icon && text) {
        // 기본=별보임(✨), 갤럭시=흰색(☀️)
        icon.innerText = isGalaxy ? "☀️" : "✨";
        text.innerText = isGalaxy ? "テーマ" : "テーマ";
    }
}


/**
 * 3. 초기 로드 및 이벤트 연결
 */
/*document.addEventListener('DOMContentLoaded', () => {
    const savedTheme = localStorage.getItem('selected-theme');
    const isGalaxy = (savedTheme === 'galaxy');

    // 초기 상태 복원
    if (isGalaxy) {
        document.body.classList.add('galaxy-theme');
    }

    updateThemeUI(isGalaxy);

    // 버튼 이벤트 바인딩
    const themeBtn = document.querySelector('.theme-neon-btn');
    if (themeBtn) {
        themeBtn.addEventListener('click', toggleTheme);
    }
});*/

/**
 * 3. 초기 로드
 */
document.addEventListener('DOMContentLoaded', () => {
    const savedTheme = localStorage.getItem('selected-theme');
    const isGalaxy = (savedTheme === 'galaxy');

    // 초기 상태 복원
    if (isGalaxy) {
        document.body.classList.add('galaxy-theme');
    }

    updateThemeUI(isGalaxy);

    // addEventListener 제거 - onclick으로 처리
});


/**
 * 4. 메시지 수신 시 효과 (선택사항)
 */
function onReceiveMessage(data) {
    const isGalaxyMode = document.body.classList.contains('galaxy-theme');

    // 기본 테마(별 보이는 상태)일 때만 효과
    if (!isGalaxyMode && data && data.type === "TALK") {
        const starLayer = document.querySelector('.stars');
        if (starLayer) {
            // 애니메이션 재시작 효과
            starLayer.style.animation = 'none';
            void starLayer.offsetWidth; // 리플로우 강제
            starLayer.style.animation = 'starsMove 120s linear infinite';
        }
    }
}