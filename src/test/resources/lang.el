
@start
asm {
    @include("init.asm");
    call main;
}

fn main() u8 {
    let x: u8 = 0;
    return x;
}
