// app.js —— 全部交互逻辑（原 <script> 内容）
const scene = document.getElementById('scene');
const index = document.getElementById('index');
const about = document.getElementById('about');
const prom = document.getElementById('prom');
const connect = document.getElementById('connect');
const blog = document.getElementById('blog');

// 点击三角形 → 加/删 active 类(展开/收起)
document.querySelector('.tri').addEventListener('click', () => {
    scene.classList.toggle('active');
    scene.classList.remove('active_1');
    scene.classList.remove('active_2');
    scene.classList.remove('active_3');
    scene.classList.remove('active_4');
    scene.classList.remove('active_5');
});

// 点击首页 → 第二套导航展开/收起
index.addEventListener('click', (e) => {
    e.preventDefault();
    scene.classList.toggle('active_1');
    scene.classList.remove('active_2');
    scene.classList.remove('active_3');
    scene.classList.remove('active_4');
    scene.classList.remove('active_5');
});

// 点击关于 → 第三套导航(个人介绍)展开/收起
about.addEventListener('click', (e) => {
    e.preventDefault();
    scene.classList.toggle('active_2');
    scene.classList.remove('active_1');
    scene.classList.remove('active_3');
    scene.classList.remove('active_4');
    scene.classList.remove('active_5');
});

prom.addEventListener('click', (e) => {
    e.preventDefault();
    scene.classList.toggle('active_3');
    scene.classList.remove('active_1');
    scene.classList.remove('active_2');
    scene.classList.remove('active_4');
    scene.classList.remove('active_5');
});

connect.addEventListener('click', (e) => {
    e.preventDefault();
    scene.classList.toggle('active_4');
    scene.classList.remove('active_1');
    scene.classList.remove('active_2');
    scene.classList.remove('active_3');
    scene.classList.remove('active_5');
});

// 点击博客 → 第五套导航(GitHub 链接)展开/收起
blog.addEventListener('click', (e) => {
    e.preventDefault();
    scene.classList.toggle('active_5');
    scene.classList.remove('active_1');
    scene.classList.remove('active_2');
    scene.classList.remove('active_3');
    scene.classList.remove('active_4');
});

// 点击场景以外的地方 → 全部收起
document.addEventListener('click', (e) => {
    if (!e.target.closest('.scene')) {
        scene.classList.remove('active');
        scene.classList.remove('active_1');
        scene.classList.remove('active_2');
        scene.classList.remove('active_3');
        scene.classList.remove('active_4');
        scene.classList.remove('active_5');
    }
});

// 滚动事件: 上滑出现倒影(视差下沉), 提示文字隐藏
const hint = document.querySelector('.hint');
const reflectLayer = document.querySelector('.reflect-layer');
window.addEventListener('scroll', () => {
    const y = window.scrollY;
    const scrolled = y > 10;
    hint.classList.toggle('hide', scrolled);
    scene.classList.toggle('scrolled', scrolled);
    reflectLayer.style.transform = `translateY(${y * 1.6}px)`;
});