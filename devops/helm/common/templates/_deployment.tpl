{{- define "common.deployment" -}}
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ .Chart.Name }}
  labels:
    app: {{ .Chart.Name }}
spec:
  replicas: {{ .Values.replicaCount | default 1 }}
  revisionHistoryLimit: 5
  selector:
    matchLabels:
      app: {{ .Chart.Name }}
  template:
    metadata:
      labels:
        app: {{ .Chart.Name }}
      annotations:
        {{- if .Values.vault.enabled }}
        vault.hashicorp.com/agent-inject: "true"
        vault.hashicorp.com/role: {{ .Chart.Name }}
        vault.hashicorp.com/agent-inject-secret-config: "secret/data/interview-platform/{{ .Chart.Name }}"
        {{- end }}
    spec:
      {{- if .Values.vault.enabled }}
      serviceAccountName: {{ .Chart.Name }}-sa
      {{- end }}
      {{- if .Values.initContainers }}
      initContainers:
        {{- toYaml .Values.initContainers | nindent 8 }}
      {{- end }}
      containers:
        - name: {{ .Chart.Name }}
          image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
          imagePullPolicy: {{ .Values.image.pullPolicy | default "IfNotPresent" }}
          ports:
            - containerPort: {{ .Values.containerPort }}
          envFrom:
            - configMapRef:
                name: {{ .Chart.Name }}-config
            {{- if .Values.secretEnabled }}
            - secretRef:
                name: {{ .Chart.Name }}-secret
            {{- end }}
          {{- if .Values.probes.enabled }}
          livenessProbe:
            httpGet:
              path: {{ .Values.probes.livenessPath | default "/actuator/health/liveness" }}
              port: {{ .Values.containerPort }}
            initialDelaySeconds: {{ .Values.probes.initialDelaySeconds | default 30 }}
            periodSeconds: 10
          readinessProbe:
            httpGet:
              path: {{ .Values.probes.readinessPath | default "/actuator/health/readiness" }}
              port: {{ .Values.containerPort }}
            initialDelaySeconds: {{ .Values.probes.initialDelaySeconds | default 20 }}
            periodSeconds: 10
          {{- end }}
          resources:
            {{- toYaml .Values.resources | nindent 12 }}
        {{- if .Values.sidecars }}
        {{- toYaml .Values.sidecars | nindent 8 }}
        {{- end }}
{{- end -}}
