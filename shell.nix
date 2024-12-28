{ pkgs ? import <nixpkgs> {} }:
  pkgs.mkShell {
    # nativeBuildInputs is usually what you want -- tools you need to run
    buildInputs = with pkgs; [ 
      corretto21
      maven
      gcc
      nodejs_22
      bashInteractive
      nodePackages."@angular/cli"
    ];

    shellHook = ''
      export JAVA_HOME=${pkgs.corretto21.home}
      source <(ng completion script)
    '';
}
